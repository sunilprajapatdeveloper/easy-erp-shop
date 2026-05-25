package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.enums.*;
import nextpos.app.nextpos.exception.*;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.SaleCalculationService;
import nextpos.app.nextpos.service.interf.SaleService;
import nextpos.app.nextpos.service.interf.StockValidationService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final DiscountRepository discountRepository;
    private final SaleCalculationService saleCalculationService;
    private final StockValidationService stockValidationService;

    @Override
    @Transactional
    public SaleResponse createSale(CreateSaleRequest request) {
        Long userId = UserContext.getCurrentUserId();
        Long companyId = UserContext.getCurrentCompanyId();

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }
        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found"));

        Sale sale = Sale.builder()
                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("SALE"))
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .warehouse(warehouse)
                .customer(customer)
                .currency(currency)
                .exchangeRate(request.getExchangeRate())
                .companyId(companyId)
                .createdBy(userId)
                .shipmentStatus(request.getShipmentStatus())
                .saleStatus(request.getSaleStatus())
                .paymentStatus(PaymentStatus.PENDING)
                .source(request.getSource())
                .posTerminalId(request.getPosTerminalId())
                .cashierId(request.getCashierId())
                .dueDate(request.getDueDate())
                .note(request.getNote())
                .shippingCost(request.getShippingCost() != null ? request.getShippingCost() : BigDecimal.ZERO)
                .roundingAmount(request.getRoundingAmount() != null ? request.getRoundingAmount() : BigDecimal.ZERO)
                .paidAmountTxnCurrency(request.getPaidAmountTxnCurrency() != null ? request.getPaidAmountTxnCurrency()
                        : BigDecimal.ZERO)
                .build();

        // Set manual discount fields if provided
        if (request.getManualDiscountValue() != null && request.getManualDiscountType() != null) {
            sale.setOrderDiscountValue(request.getManualDiscountValue());
            sale.setOrderDiscountType(request.getManualDiscountType());
            sale.setDiscountSource(DiscountSource.MANUAL);
            // reason stored in discountDescription (or a dedicated field if available)
            sale.setDiscountDescription(request.getManualDiscountReason());
        }

        // Set system discount if provided
        if (request.getAppliedDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getAppliedDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            sale.setAppliedDiscount(discount);
        }

        // Set coupon
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            sale.setPromotionCouponCode(request.getCouponCode());
        }

        // Build products
        List<SaleProduct> products = request.getProducts().stream().map(p -> {
            Product product = productRepository.findById(p.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + p.getProductId()));
            return SaleProduct.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(p.getQuantity())
                    .productUnitPrice(p.getUnitPriceOverride() != null ? p.getUnitPriceOverride() : BigDecimal.ZERO) // will
                                                                                                                     // be
                                                                                                                     // resolved
                                                                                                                     // by
                                                                                                                     // PricingService
                    .createdBy(userId)
                    .companyId(companyId)
                    .build();
        }).collect(Collectors.toList());
        sale.setProducts(products);

        // All calculations
        saleCalculationService.calculate(sale);

        // Stock validation & deduction
        stockValidationService.validateAndDeduct(sale);

        Sale saved = saleRepository.save(sale);
        return SaleResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public SaleResponse updateSale(Long id, UpdateSaleRequest request) {
        Long userId = UserContext.getCurrentUserId();
        Long companyId = UserContext.getCurrentCompanyId();

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        if (!sale.getCompanyId().equals(companyId)) {
            throw new SecurityException("Access denied");
        }

        // Reverse old stock
        stockValidationService.reverseDeduction(sale);

        // Update scalar fields
        if (request.getDate() != null)
            sale.setDate(request.getDate());
        if (request.getCustomerId() != null) {
            sale.setCustomer(customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
        }
        if (request.getWarehouseId() != null) {
            sale.setWarehouse(warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found")));
        }
        if (request.getCurrencyId() != null) {
            sale.setCurrency(currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency not found")));
        }
        if (request.getExchangeRate() != null)
            sale.setExchangeRate(request.getExchangeRate());

        // Discount handling
        if (request.getManualDiscountValue() != null && request.getManualDiscountType() != null) {
            sale.setOrderDiscountValue(request.getManualDiscountValue());
            sale.setOrderDiscountType(request.getManualDiscountType());
            sale.setDiscountSource(DiscountSource.MANUAL);
            sale.setDiscountDescription(request.getManualDiscountReason());
            sale.setAppliedDiscount(null); // clear system discount
        }
        if (request.getAppliedDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getAppliedDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            sale.setAppliedDiscount(discount);
            // clear manual discount
            sale.setOrderDiscountValue(null);
            sale.setOrderDiscountType(null);
            sale.setDiscountSource(DiscountSource.SYSTEM);
            sale.setDiscountDescription(null);
        }

        // Promotion
        if (request.getCouponCode() != null) {
            sale.setPromotionCouponCode(request.getCouponCode().isBlank() ? null : request.getCouponCode());
        }

        // Other fields
        if (request.getShippingCost() != null)
            sale.setShippingCost(request.getShippingCost());
        if (request.getRoundingAmount() != null)
            sale.setRoundingAmount(request.getRoundingAmount());
        if (request.getShipmentStatus() != null)
            sale.setShipmentStatus(request.getShipmentStatus());
        if (request.getSaleStatus() != null)
            sale.setSaleStatus(request.getSaleStatus());
        if (request.getPaymentStatus() != null)
            sale.setPaymentStatus(request.getPaymentStatus());
        if (request.getSource() != null)
            sale.setSource(request.getSource());
        if (request.getNote() != null)
            sale.setNote(request.getNote());
        if (request.getPosTerminalId() != null)
            sale.setPosTerminalId(request.getPosTerminalId());
        if (request.getCashierId() != null)
            sale.setCashierId(request.getCashierId());
        if (request.getDueDate() != null)
            sale.setDueDate(request.getDueDate());

        // Update products (if provided)
        if (request.getProducts() != null) {
            // Remove products no longer present
            Set<Long> newProductIds = request.getProducts().stream()
                    .map(UpdateSaleRequest.SaleProductUpdateRequest::getProductId)
                    .collect(Collectors.toSet());
            sale.getProducts().removeIf(sp -> !newProductIds.contains(sp.getProduct().getId()));

            // Add / update
            for (UpdateSaleRequest.SaleProductUpdateRequest p : request.getProducts()) {
                SaleProduct existing = sale.getProducts().stream()
                        .filter(sp -> sp.getProduct().getId().equals(p.getProductId()))
                        .findFirst().orElse(null);
                if (existing != null) {
                    if (p.getQuantity() != null)
                        existing.setQuantity(p.getQuantity());
                    if (p.getUnitPriceOverride() != null)
                        existing.setProductUnitPrice(p.getUnitPriceOverride());
                    existing.setUpdatedBy(userId);
                } else {
                    Product product = productRepository.findById(p.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    SaleProduct newSp = SaleProduct.builder()
                            .sale(sale)
                            .product(product)
                            .quantity(p.getQuantity() != null ? p.getQuantity() : 1)
                            .productUnitPrice(
                                    p.getUnitPriceOverride() != null ? p.getUnitPriceOverride() : BigDecimal.ZERO)
                            .createdBy(userId)
                            .companyId(companyId)
                            .build();
                    sale.getProducts().add(newSp);
                }
            }
        }

        sale.setUpdatedBy(userId);

        // Recalculate everything
        saleCalculationService.calculate(sale);

        // Deduct new stock
        stockValidationService.validateAndDeduct(sale);

        Sale saved = saleRepository.save(sale);
        return SaleResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        checkCompanyAccess(sale);
        return SaleResponse.fromEntity(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getMySales() {
        Long userId = UserContext.getCurrentUserId();
        return saleRepository.findAllByCreatedBy(userId).stream()
                .map(SaleResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAllSales() {
        Long companyId = UserContext.getCurrentCompanyId();
        return saleRepository.findAllByCompanyId(companyId).stream()
                .map(SaleResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        checkCompanyAccess(sale);
        stockValidationService.reverseDeduction(sale);
        saleRepository.delete(sale);
    }

    private void checkCompanyAccess(Sale sale) {
        Long currentCompanyId = UserContext.getCurrentCompanyId();
        if (!sale.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Access denied to this sale");
        }
    }

    @Override
    public List<SaleResponse> findRecentSalesByTenant(Long tenantId, int limit) {
        List<Sale> sales = saleRepository.findTop5ByCompanyIdOrderByCreatedAtDesc(tenantId);
        return sales.stream().map(SaleResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getSalesSummary(String period, Long warehouseId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSales", 10000.0);
        summary.put("totalOrders", 50);
        return summary;
    }
}