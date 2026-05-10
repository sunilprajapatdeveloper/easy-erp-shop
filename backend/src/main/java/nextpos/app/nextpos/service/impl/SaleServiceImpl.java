package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.InvalidPromotionException;
import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.enums.*;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PromotionEngineService;
import nextpos.app.nextpos.service.interf.SaleService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductStockService productStockService;
    private final PromotionEngineService promotionEngineService;
    private final PromotionRepository promotionRepository;

    @Override
    @Transactional
    public SaleResponse createSale(CreateSaleRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        // Validate core entities
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }
        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(() -> new RuntimeException("Currency not found"));
        BigDecimal exchangeRate = request.getExchangeRate();
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valid exchange rate is required");
        }

        // Build Sale skeleton
        Sale sale = Sale.builder()
                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("SALE"))
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .warehouse(warehouse)
                .customer(customer)
                .currency(currency)
                .exchangeRate(exchangeRate)
                .companyId(currentCompanyId)
                .createdBy(currentUserId)
                .createdAt(LocalDateTime.now())
                .shipmentStatus(Optional.ofNullable(request.getShipmentStatus()).orElse(ShipmentStatus.PENDING))
                .saleStatus(Optional.ofNullable(request.getSaleStatus()).orElse(SaleStatus.PENDING))
                .source(Optional.ofNullable(request.getSource()).orElse(SaleSource.WEB))
                .note(request.getNote())
                .orderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO))
                .orderDiscount(Optional.ofNullable(request.getOrderDiscount()).orElse(BigDecimal.ZERO))
                .orderDiscountType(request.getOrderDiscountType())
                .shippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO))
                .roundingAmount(BigDecimal.ZERO)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        // Process products and adjust stock
        BigDecimal subtotalTxn = BigDecimal.ZERO;
        List<SaleProduct> saleProducts = new ArrayList<>();

        for (CreateSaleRequest.SaleProductRequest p : request.getProducts()) {
            Product product = productRepository.findById(p.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + p.getProductId()));

            int qty = Optional.ofNullable(p.getQuantity()).orElse(0);
            // Stock check & reduction
            ProductStockResponse stock = productStockService.getByProductAndWarehouse(product.getId(),
                    warehouse.getId());
            if (stock.getQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            productStockService.adjustStock(product.getId(), warehouse.getId(), -qty);

            BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice()).orElse(BigDecimal.ZERO);
            BigDecimal lineDiscount = Optional.ofNullable(p.getDiscount()).orElse(BigDecimal.ZERO);
            BigDecimal lineSubtotal = p.getSubTotal(); // Expected: (unitPrice * qty) - lineDiscount

            subtotalTxn = subtotalTxn.add(lineSubtotal);

            SaleProduct sp = SaleProduct.builder()
                    .sale(sale)
                    .product(product)
                    .productUnitPrice(unitPrice)
                    .quantity(qty)
                    .discount(lineDiscount)
                    .subTotal(lineSubtotal)
                    .taxName(p.getTaxName())
                    .taxCategory(p.getTaxCategory())
                    .taxRate(p.getTaxRate())
                    .taxInclusionType(p.getTaxInclusionType())
                    .taxApplicationOrder(p.getTaxApplicationOrder())
                    .taxAmount(p.getTaxAmount())
                    .createdBy(currentUserId)
                    .createdAt(LocalDateTime.now())
                    .companyId(currentCompanyId)
                    .build();
            saleProducts.add(sp);
        }
        sale.setProducts(saleProducts);

        // Calculate preliminary totals (without promotion)
        BigDecimal totalBeforePromo = subtotalTxn
                .add(sale.getOrderTax())
                .subtract(sale.getOrderDiscount())
                .add(sale.getShippingCost())
                .add(sale.getRoundingAmount());

        sale.setTotalAmountTxnCurrency(totalBeforePromo);
        sale.setPaidAmountTxnCurrency(BigDecimal.ZERO);
        sale.setDueAmountTxnCurrency(totalBeforePromo);
        sale.setGrandTotalTxnCurrency(totalBeforePromo);

        // Apply promotion if coupon code provided
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            applyPromotionToSale(sale, request.getCouponCode(), request.getCurrencyCode(),
                    request.getWarehouseId(), currentCompanyId, request.getCustomerId(), saleProducts);
        }

        // Recalculate final totals after promotion
        BigDecimal finalTotal = recalculateSaleTotal(sale);
        sale.setTotalAmountTxnCurrency(finalTotal);
        sale.setGrandTotalTxnCurrency(finalTotal);
        sale.setDueAmountTxnCurrency(finalTotal.subtract(sale.getPaidAmountTxnCurrency()));

        // Base currency conversion
        BigDecimal totalBase = finalTotal.multiply(exchangeRate);
        sale.setTotalAmountBaseCurrency(totalBase);
        sale.setDueAmountBaseCurrency(totalBase.subtract(sale.getPaidAmountBaseCurrency()));

        // Save and return
        Sale savedSale = saleRepository.save(sale);
        return SaleResponse.fromEntity(savedSale);
    }

    private void applyPromotionToSale(Sale sale, String couponCode, String currencyCode,
            Long warehouseId, Long companyId, Long customerId,
            List<SaleProduct> saleProducts) {

        List<CartItemDto> cartItems = saleProducts.stream()
                .map(sp -> new CartItemDto(sp.getProduct().getId(), sp.getQuantity(), sp.getProductUnitPrice()))
                .collect(Collectors.toList());

        CouponValidationRequest validationRequest = new CouponValidationRequest();
        validationRequest.setCouponCode(couponCode);
        validationRequest.setCustomerId(customerId);
        validationRequest.setWarehouseId(warehouseId);
        validationRequest.setCompanyId(companyId);
        validationRequest.setCurrencyCode(currencyCode);
        validationRequest.setItems(cartItems);
        validationRequest.setShippingCost(sale.getShippingCost());

        CouponValidationResponse validation = promotionEngineService.validateCoupon(validationRequest);
        if (!validation.isValid()) {
            throw new InvalidPromotionException(validation.getMessage());
        }

        if (validation.getAppliedPromotionId() != null) {
            Promotion promo = promotionRepository.getReferenceById(validation.getAppliedPromotionId());
            sale.setAppliedPromotion(promo);
        }
        sale.setPromotionDiscountAmount(validation.getDiscountAmount());
        sale.setPromotionDiscountType(validation.getDiscountType());
        sale.setPromotionCouponCode(couponCode);

        if (validation.isFreeShipping()) {
            sale.setShippingCost(BigDecimal.ZERO);
        }

        if (validation.getAppliedPromotionId() != null && sale.getId() != null) {
            promotionEngineService.recordPromotionUsage(validation.getAppliedPromotionId(),
                    sale.getId(), customerId, companyId);
        }
    }

    private BigDecimal recalculateSaleTotal(Sale sale) {
        BigDecimal subtotal = sale.getProducts().stream()
                .map(SaleProduct::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal afterManualDiscount = subtotal.subtract(sale.getOrderDiscount());
        BigDecimal afterPromotion = afterManualDiscount.subtract(
                sale.getPromotionDiscountAmount() != null ? sale.getPromotionDiscountAmount() : BigDecimal.ZERO);
        return afterPromotion
                .add(sale.getOrderTax())
                .add(sale.getShippingCost())
                .add(sale.getRoundingAmount());
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        Long currentCompanyId = UserContext.getCurrentCompanyId();
        if (!sale.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Access denied to this sale");
        }
        return SaleResponse.fromEntity(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getMySales() {
        Long currentUserId = UserContext.getCurrentUserId();
        List<Sale> sales = saleRepository.findAllByCreatedBy(currentUserId);
        return sales.stream().map(SaleResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAllSales() {
        Long currentCompanyId = UserContext.getCurrentCompanyId();
        List<Sale> sales = saleRepository.findAllByCompanyId(currentCompanyId);
        return sales.stream().map(SaleResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleResponse updateSale(Long id, UpdateSaleRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        if (!sale.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Access denied to this sale");
        }

        // Restore stock for old products
        Warehouse oldWarehouse = sale.getWarehouse();
        for (SaleProduct sp : sale.getProducts()) {
            productStockService.adjustStock(sp.getProduct().getId(), oldWarehouse.getId(), sp.getQuantity());
        }

        // Update warehouse if changed
        if (request.getWarehouseId() != null && !request.getWarehouseId().equals(oldWarehouse.getId())) {
            Warehouse newWarehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("New warehouse not found"));
            sale.setWarehouse(newWarehouse);
        }
        Warehouse finalWarehouse = sale.getWarehouse();

        // Process products
        Set<Long> requestProductIds = request.getProducts() != null
                ? request.getProducts().stream().map(UpdateSaleRequest.SaleProductUpdateRequest::getProductId)
                        .collect(Collectors.toSet())
                : new HashSet<>();

        List<SaleProduct> updatedProducts = new ArrayList<>();
        BigDecimal subtotalTxn = BigDecimal.ZERO;

        if (request.getProducts() != null) {
            for (UpdateSaleRequest.SaleProductUpdateRequest p : request.getProducts()) {
                Product product = productRepository.findById(p.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + p.getProductId()));
                int qty = Optional.ofNullable(p.getQuantity()).orElse(0);

                ProductStockResponse stock = productStockService.getByProductAndWarehouse(product.getId(),
                        finalWarehouse.getId());
                if (stock.getQuantity() < qty) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }
                productStockService.adjustStock(product.getId(), finalWarehouse.getId(), -qty);

                SaleProduct existing = sale.getProducts().stream()
                        .filter(sp -> sp.getProduct().getId().equals(product.getId()))
                        .findFirst().orElse(null);

                BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice()).orElse(BigDecimal.ZERO);
                BigDecimal lineDiscount = Optional.ofNullable(p.getDiscount()).orElse(BigDecimal.ZERO);
                BigDecimal lineSubtotal = p.getSubTotal() != null ? p.getSubTotal()
                        : unitPrice.multiply(BigDecimal.valueOf(qty)).subtract(lineDiscount);

                subtotalTxn = subtotalTxn.add(lineSubtotal);

                if (existing != null) {
                    existing.setQuantity(qty);
                    existing.setProductUnitPrice(unitPrice);
                    existing.setDiscount(lineDiscount);
                    existing.setSubTotal(lineSubtotal);
                    if (p.getTaxName() != null)
                        existing.setTaxName(p.getTaxName());
                    if (p.getTaxCategory() != null)
                        existing.setTaxCategory(p.getTaxCategory());
                    if (p.getTaxRate() != null)
                        existing.setTaxRate(p.getTaxRate());
                    if (p.getTaxInclusionType() != null)
                        existing.setTaxInclusionType(p.getTaxInclusionType());
                    if (p.getTaxApplicationOrder() != null)
                        existing.setTaxApplicationOrder(p.getTaxApplicationOrder());
                    if (p.getTaxAmount() != null)
                        existing.setTaxAmount(p.getTaxAmount());
                    existing.setUpdatedBy(currentUserId);
                    existing.setUpdatedAt(LocalDateTime.now());
                    updatedProducts.add(existing);
                } else {
                    SaleProduct newSp = SaleProduct.builder()
                            .sale(sale)
                            .product(product)
                            .productUnitPrice(unitPrice)
                            .quantity(qty)
                            .discount(lineDiscount)
                            .subTotal(lineSubtotal)
                            .taxName(p.getTaxName())
                            .taxCategory(p.getTaxCategory())
                            .taxRate(p.getTaxRate())
                            .taxInclusionType(p.getTaxInclusionType())
                            .taxApplicationOrder(p.getTaxApplicationOrder())
                            .taxAmount(p.getTaxAmount())
                            .createdBy(currentUserId)
                            .createdAt(LocalDateTime.now())
                            .companyId(currentCompanyId)
                            .build();
                    updatedProducts.add(newSp);
                }
            }
        }

        // Remove products not in request
        if (!requestProductIds.isEmpty()) {
            sale.getProducts().removeIf(sp -> !requestProductIds.contains(sp.getProduct().getId()));
        }
        sale.setProducts(updatedProducts);

        // Update scalar fields (note discount fields renamed)
        Optional.ofNullable(request.getDate()).ifPresent(sale::setDate);
        Optional.ofNullable(request.getOrderTax()).ifPresent(sale::setOrderTax);
        Optional.ofNullable(request.getOrderDiscount()).ifPresent(sale::setOrderDiscount);
        Optional.ofNullable(request.getOrderDiscountType()).ifPresent(sale::setOrderDiscountType);
        Optional.ofNullable(request.getShippingCost()).ifPresent(sale::setShippingCost);
        Optional.ofNullable(request.getShipmentStatus()).ifPresent(sale::setShipmentStatus);
        Optional.ofNullable(request.getSaleStatus()).ifPresent(sale::setSaleStatus);
        Optional.ofNullable(request.getSource()).ifPresent(sale::setSource);
        Optional.ofNullable(request.getNote()).ifPresent(sale::setNote);
        if (request.getCustomerId() != null) {
            Customer newCustomer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            sale.setCustomer(newCustomer);
        }
        if (request.getCurrencyId() != null) {
            Currency newCurrency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new RuntimeException("Currency not found"));
            sale.setCurrency(newCurrency);
        }
        if (request.getExchangeRate() != null) {
            sale.setExchangeRate(request.getExchangeRate());
        }

        sale.setUpdatedBy(currentUserId);
        sale.setUpdatedAt(LocalDateTime.now());

        // Recalculate totals
        BigDecimal total = recalculateSaleTotal(sale);
        sale.setTotalAmountTxnCurrency(total);
        sale.setGrandTotalTxnCurrency(total);
        sale.setDueAmountTxnCurrency(total.subtract(sale.getPaidAmountTxnCurrency()));

        BigDecimal totalBase = total.multiply(sale.getExchangeRate());
        sale.setTotalAmountBaseCurrency(totalBase);
        sale.setDueAmountBaseCurrency(totalBase.subtract(sale.getPaidAmountBaseCurrency()));

        Sale saved = saleRepository.save(sale);
        return SaleResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        Long currentCompanyId = UserContext.getCurrentCompanyId();
        if (!sale.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Access denied to this sale");
        }
        for (SaleProduct sp : sale.getProducts()) {
            productStockService.adjustStock(sp.getProduct().getId(), sale.getWarehouse().getId(), sp.getQuantity());
        }
        saleRepository.delete(sale);
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
