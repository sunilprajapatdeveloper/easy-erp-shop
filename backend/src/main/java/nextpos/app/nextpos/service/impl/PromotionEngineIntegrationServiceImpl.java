package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;
import nextpos.app.nextpos.model.entity.Promotion;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.repository.PromotionRepository;
import nextpos.app.nextpos.service.interf.PromotionEngineIntegrationService;
import nextpos.app.nextpos.service.interf.PromotionEngineService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionEngineIntegrationServiceImpl implements PromotionEngineIntegrationService {

    private final PromotionEngineService promotionEngineService;
    private final PromotionRepository promotionRepository;

    @Override
    public void applyPromotion(Sale sale) {
        if (sale.getPromotionCouponCode() == null || sale.getPromotionCouponCode().isBlank()) {
            return;
        }

        // Build validation request from sale
        CouponValidationRequest request = new CouponValidationRequest();
        request.setCouponCode(sale.getPromotionCouponCode());
        request.setCustomerId(sale.getCustomer() != null ? sale.getCustomer().getId() : null);
        request.setWarehouseId(sale.getWarehouse().getId());
        request.setCompanyId(sale.getCompanyId());
        request.setCurrencyCode(sale.getCurrency().getCode());
        request.setItems(sale.getProducts().stream()
                .map(sp -> new CartItemDto(sp.getProduct().getId(), sp.getQuantity(), sp.getProductUnitPrice()))
                .collect(Collectors.toList()));
        request.setShippingCost(sale.getShippingCost());

        CouponValidationResponse validation = promotionEngineService.validateCoupon(request);
        if (!validation.isValid()) {
            throw new RuntimeException("Invalid promotion: " + validation.getMessage());
        }

        // Set promotion snapshot on sale
        sale.setPromotionDiscountAmount(validation.getDiscountAmount());
        sale.setPromotionDiscountType(validation.getDiscountType());
        // Use getAppliedDiscountValue() instead of getDiscountValue()
        sale.setPromotionDiscountValue(validation.getAppliedDiscountValue());
        sale.setPromotionCouponCode(sale.getPromotionCouponCode());

        if (validation.getAppliedPromotionId() != null) {
            Promotion promo = promotionRepository.findByIdAndCompanyId(validation.getAppliedPromotionId(), sale.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));
            sale.setAppliedPromotion(promo);
            sale.setPromotionName(promo.getName());
            sale.setPromotionCode(promo.getCode());
            sale.setPromotionDescription(promo.getDescription());
            // Use promo.getType() instead of promo.getPromotionType()
            sale.setPromotionType(promo.getType());
        }

        // Free shipping override
        if (validation.isFreeShipping()) {
            sale.setShippingCost(BigDecimal.ZERO);
        }

        // Usage recording will be done after the sale is persisted, in SaleService
    }
}
