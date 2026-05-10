package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.response.AppliedProductDiscount;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.CustomerGroup;
import nextpos.app.nextpos.model.enums.PromotionType;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.service.helper.CurrencyConverter;
import nextpos.app.nextpos.service.interf.PromotionEngineService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionEngineServiceImpl implements PromotionEngineService {

    private final PromotionRepository promotionRepository;
    private final PromotionUsageRepository usageRepository;
    private final PromotionProductRepository productRepository;
    private final PromotionCategoryRepository categoryRepository;
    private final PromotionCustomerGroupRepository customerGroupRepository;
    private final CurrencyConverter currencyConverter;
    private final CustomerRepository customerRepository;
    private final CompanyCurrencyRepository companyCurrencyRepository;

    @Override
    @Cacheable(value = "promotionValidation", key = "#request.couponCode + '_' + #request.customerId + '_' + #request.warehouseId")
    public CouponValidationResponse validateCoupon(CouponValidationRequest request) {
        Promotion promotion = promotionRepository.findByCompanyIdAndCodeAndIsActiveTrue(
                request.getCompanyId(), request.getCouponCode()).orElse(null);

        if (promotion == null) {
            return invalidResponse("Invalid coupon code");
        }
        String validationError = validatePromotion(promotion, request);
        if (validationError != null) {
            return invalidResponse(validationError);
        }
        return calculateDiscount(promotion, request);
    }

    @Override
    public List<CouponValidationResponse> getAutoApplicablePromotions(Long companyId, Long warehouseId,
            Long customerId, List<CartItemDto> items,
            BigDecimal shippingCost, String currencyCode) {
        List<Promotion> autoPromotions = promotionRepository.findActiveByType(
                companyId, warehouseId, PromotionType.AUTO, LocalDateTime.now());
        List<CouponValidationResponse> applicable = new ArrayList<>();
        for (Promotion promo : autoPromotions) {
            CouponValidationRequest req = new CouponValidationRequest();
            req.setCompanyId(companyId);
            req.setWarehouseId(warehouseId);
            req.setCustomerId(customerId);
            req.setItems(items);
            req.setShippingCost(shippingCost);
            req.setCurrencyCode(currencyCode);
            String error = validatePromotion(promo, req);
            if (error == null) {
                applicable.add(calculateDiscount(promo, req));
            }
        }
        return applyStackingStrategy(applicable);
    }

    @Override
    @Transactional
    public void recordPromotionUsage(Long promotionId, Long saleId, Long customerId, Long companyId) {
        PromotionUsage usage = PromotionUsage.builder()
                .promotion(promotionRepository.getReferenceById(promotionId))
                .companyId(companyId)
                .customerId(customerId)
                .saleId(saleId)
                .usageCount(1)
                .build();
        usageRepository.save(usage);
    }

    // ---- Private helpers ----

    private String validatePromotion(Promotion promotion, CouponValidationRequest request) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getStartDate())
                || (promotion.getEndDate() != null && now.isAfter(promotion.getEndDate()))) {
            return "Promotion is not active in current date range";
        }
        if (!promotion.getIsActive()) {
            return "Promotion is inactive";
        }
        if (promotion.getWarehouseId() != null && !promotion.getWarehouseId().equals(request.getWarehouseId())) {
            return "Promotion not applicable for this warehouse";
        }
        // Customer group check
        if (request.getCustomerId() != null) {
            List<CustomerGroup> allowedGroups = customerGroupRepository.findGroupsByPromotionId(promotion.getId());
            if (!allowedGroups.isEmpty()) {
                CustomerGroup customerGroup = getCustomerGroup(request.getCustomerId());
                if (!allowedGroups.contains(customerGroup)) {
                    return "Promotion not eligible for this customer group";
                }
            }
        }
        // Usage limits
        int totalUsed = usageRepository.getTotalUsageCount(promotion.getId());
        if (promotion.getUsageLimit() != null && totalUsed >= promotion.getUsageLimit()) {
            return "Promotion usage limit exceeded";
        }
        if (request.getCustomerId() != null && promotion.getUsageLimitPerCustomer() != null) {
            int customerUsed = usageRepository.getCustomerUsageCount(promotion.getId(), request.getCustomerId());
            if (customerUsed >= promotion.getUsageLimitPerCustomer()) {
                return "Customer usage limit exceeded";
            }
        }
        // Order amount conditions
        BigDecimal subtotalBase = calculateSubtotalBase(request.getItems(), request.getCurrencyCode(),
                promotion.getCompanyId(), request.getWarehouseId());
        if (promotion.getMinOrderAmount() != null && subtotalBase.compareTo(promotion.getMinOrderAmount()) < 0) {
            return "Order amount below minimum required";
        }
        if (promotion.getMaxOrderAmount() != null && subtotalBase.compareTo(promotion.getMaxOrderAmount()) > 0) {
            return "Order amount exceeds maximum allowed";
        }
        // Product/Category targeting
        if (promotion.getType() != PromotionType.FREE_SHIPPING) {
            List<Long> promoProductIds = productRepository.findProductIdsByPromotionId(promotion.getId());
            List<Long> promoCategoryIds = categoryRepository.findCategoryIdsByPromotionId(promotion.getId());
            if (!promoProductIds.isEmpty() || !promoCategoryIds.isEmpty()) {
                boolean eligibleProductFound = request.getItems().stream()
                        .anyMatch(item -> promoProductIds.contains(item.getProductId())
                                || isProductInCategories(item.getProductId(), promoCategoryIds));
                if (!eligibleProductFound) {
                    return "No eligible product in cart for this promotion";
                }
            }
        }
        return null;
    }

    private CouponValidationResponse calculateDiscount(Promotion promotion, CouponValidationRequest request) {
        BigDecimal discountAmountBase = BigDecimal.ZERO;
        boolean freeShipping = false;
        List<AppliedProductDiscount> productDiscounts = new ArrayList<>();

        switch (promotion.getDiscountType()) {
            case PERCENTAGE:
                BigDecimal subtotalBase = calculateSubtotalBase(request.getItems(), request.getCurrencyCode(),
                        promotion.getCompanyId(), request.getWarehouseId());
                discountAmountBase = subtotalBase
                        .multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100)));
                if (promotion.getMaxDiscountAmount() != null
                        && discountAmountBase.compareTo(promotion.getMaxDiscountAmount()) > 0) {
                    discountAmountBase = promotion.getMaxDiscountAmount();
                }
                break;
            case FIXED:
                discountAmountBase = promotion.getDiscountValue();
                break;
            case FREE_ITEM:
                if (promotion.getType() == PromotionType.BUY_X_GET_Y) {
                    discountAmountBase = calculateBuyXGetYDiscount(promotion, request, productDiscounts);
                }
                break;
            case FREE_SHIPPING:
                freeShipping = true;
                discountAmountBase = BigDecimal.ZERO;
                break;
        }

        BigDecimal discountAmountTxn = BigDecimal.ZERO;
        if (discountAmountBase.compareTo(BigDecimal.ZERO) > 0) {
            discountAmountTxn = currencyConverter.fromBaseCurrency(discountAmountBase,
                    promotion.getCompanyId(), request.getWarehouseId(), request.getCurrencyCode());
        }

        return CouponValidationResponse.builder()
                .valid(true)
                .message("Promotion applied")
                .discountAmount(discountAmountTxn)
                .discountType(promotion.getDiscountType())
                .appliedDiscountValue(promotion.getDiscountValue())
                .appliedPromotionId(promotion.getId())
                .appliedPromotionName(promotion.getName())
                .productDiscounts(productDiscounts)
                .freeShipping(freeShipping)
                .build();
    }

    private BigDecimal calculateBuyXGetYDiscount(Promotion promotion, CouponValidationRequest request,
            List<AppliedProductDiscount> productDiscounts) {
        Long targetProductId = promotion.getBuyProduct() != null ? promotion.getBuyProduct().getId() : null;
        List<Long> targetCategoryIds = categoryRepository.findCategoryIdsByPromotionId(promotion.getId());
        BigDecimal totalDiscountBase = BigDecimal.ZERO;

        for (CartItemDto item : request.getItems()) {
            boolean eligible = (targetProductId != null && item.getProductId().equals(targetProductId)) ||
                    isProductInCategories(item.getProductId(), targetCategoryIds);
            if (eligible) {
                int eligibleSets = item.getQuantity() / promotion.getBuyQuantity();
                int freeItems = eligibleSets * promotion.getGetQuantity();
                if (freeItems > 0) {
                    BigDecimal unitPriceBase = currencyConverter.toBaseCurrency(item.getUnitPrice(),
                            promotion.getCompanyId(), request.getWarehouseId(), request.getCurrencyCode());
                    BigDecimal discountBase = unitPriceBase.multiply(BigDecimal.valueOf(freeItems));
                    if (promotion.getGetDiscountPercent() != null) {
                        discountBase = discountBase
                                .multiply(promotion.getGetDiscountPercent().divide(BigDecimal.valueOf(100)));
                    }
                    totalDiscountBase = totalDiscountBase.add(discountBase);
                    productDiscounts.add(AppliedProductDiscount.builder()
                            .productId(item.getProductId())
                            .discountAmount(discountBase)
                            .description(String.format("Buy %d get %d free", promotion.getBuyQuantity(),
                                    promotion.getGetQuantity()))
                            .build());
                }
            }
        }
        return totalDiscountBase;
    }

    private BigDecimal calculateSubtotalBase(List<CartItemDto> items, String txnCurrency, Long companyId,
            Long warehouseId) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItemDto item : items) {
            BigDecimal lineTotalTxn = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal lineTotalBase = currencyConverter.convert(lineTotalTxn, companyId, warehouseId,
                    txnCurrency, getBaseCurrencyCode(companyId));
            subtotal = subtotal.add(lineTotalBase);
        }
        return subtotal;
    }

    private List<CouponValidationResponse> applyStackingStrategy(List<CouponValidationResponse> applicable) {
        if (applicable.isEmpty())
            return applicable;
        Optional<CouponValidationResponse> freeShipping = applicable.stream()
                .filter(CouponValidationResponse::isFreeShipping).findFirst();
        if (freeShipping.isPresent())
            return List.of(freeShipping.get());
        return List.of(applicable.stream()
                .max(Comparator.comparing(CouponValidationResponse::getDiscountAmount))
                .orElse(applicable.get(0)));
    }

    private CustomerGroup getCustomerGroup(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getCustomerGroup)
                .orElse(CustomerGroup.RETAIL);
    }

    private boolean isProductInCategories(Long productId, List<Long> categoryIds) {
        // Implement actual category check using ProductCategoryRepository if needed
        return false;
    }

    private String getBaseCurrencyCode(Long companyId) {
        return companyCurrencyRepository.findByCompanyId(companyId)
                .stream()
                .filter(CompanyCurrency::isDefaultCurrency)
                .findFirst()
                .map(cc -> cc.getCurrency().getCode())
                .orElseThrow(() -> new IllegalStateException("No default currency for company: " + companyId));
    }

    private CouponValidationResponse invalidResponse(String message) {
        return CouponValidationResponse.builder()
                .valid(false)
                .message(message)
                .discountAmount(BigDecimal.ZERO)
                .build();
    }
}