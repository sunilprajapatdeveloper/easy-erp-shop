package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.InvalidPromotionException;
import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.dto.request.CreateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;
import nextpos.app.nextpos.model.dto.response.SaleReturnResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.enums.*;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PromotionEngineService;
import nextpos.app.nextpos.service.interf.SaleReturnService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleReturnServiceImpl implements SaleReturnService {

        private final SaleReturnRepository saleReturnRepository;
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
        public SaleReturnResponse createSaleReturn(CreateSaleReturnRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Sale originalSale = saleRepository.findById(request.getOriginalSaleId())
                                .orElseThrow(() -> new RuntimeException("Original sale not found"));

                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new RuntimeException("Currency not found"));

                BigDecimal exchangeRate = request.getExchangeRate();
                if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("Valid exchange rate is required");
                }

                SaleReturn saleReturn = SaleReturn.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("SALERETURN"))
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .originalSale(originalSale)
                                .customer(customer)
                                .warehouse(warehouse)
                                .currency(currency)
                                .exchangeRate(exchangeRate)
                                .orderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO))
                                .orderDiscount(Optional.ofNullable(request.getOrderDiscount()).orElse(BigDecimal.ZERO))
                                .orderDiscountType(request.getOrderDiscountType())
                                .shippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO))
                                .roundingAmount(Optional.ofNullable(request.getRoundingAmount())
                                                .orElse(BigDecimal.ZERO))
                                .shipmentStatus(Optional.ofNullable(request.getShipmentStatus())
                                                .orElse(ShipmentStatus.PENDING))
                                .saleStatus(Optional.ofNullable(request.getSaleStatus()).orElse(SaleStatus.PENDING))
                                .source(Optional.ofNullable(request.getSource()).orElse(SaleSource.WEB))
                                .paymentStatus(Optional.ofNullable(request.getPaymentStatus())
                                                .orElse(PaymentStatus.PENDING))
                                .note(request.getNote())
                                .companyId(currentCompanyId)
                                .createdBy(currentUserId)
                                .createdAt(LocalDateTime.now())
                                .build();

                // Process products and adjust stock (increase stock for returned items)
                BigDecimal subtotalSum = BigDecimal.ZERO;
                List<SaleReturnProduct> returnProducts = new ArrayList<>();

                for (CreateSaleReturnRequest.SaleReturnProductRequest p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = p.getQuantity();
                        // Return to stock: increase
                        productStockService.adjustStock(product.getId(), warehouse.getId(), qty);

                        BigDecimal unitPrice = p.getProductUnitPrice();
                        BigDecimal discount = p.getDiscount();
                        BigDecimal subTotal = p.getSubTotal(); // (unitPrice * qty) - discount
                        subtotalSum = subtotalSum.add(subTotal);

                        SaleReturnProduct sp = SaleReturnProduct.builder()
                                        .saleReturn(saleReturn)
                                        .product(product)
                                        .productUnitPrice(unitPrice)
                                        .quantity(qty)
                                        .discount(discount)
                                        .subTotal(subTotal)
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
                        returnProducts.add(sp);
                }
                saleReturn.setProducts(returnProducts);

                // Calculate preliminary total (without promotion)
                BigDecimal totalBeforePromo = subtotalSum
                                .add(saleReturn.getOrderTax())
                                .subtract(saleReturn.getOrderDiscount())
                                .add(saleReturn.getShippingCost())
                                .add(saleReturn.getRoundingAmount());

                saleReturn.setTotalAmountTxnCurrency(totalBeforePromo);
                saleReturn.setPaidAmountTxnCurrency(BigDecimal.ZERO);
                saleReturn.setDueAmountTxnCurrency(totalBeforePromo);
                saleReturn.setGrandTotalTxnCurrency(totalBeforePromo);

                // Apply promotion if coupon code provided
                if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
                        applyPromotionToSaleReturn(saleReturn, request.getCouponCode(),
                                        request.getCurrencyId(), request.getWarehouseId(),
                                        currentCompanyId, request.getCustomerId(), returnProducts);
                }

                // Recalculate final totals after promotion
                BigDecimal finalTotal = recalculateSaleReturnTotal(saleReturn);
                saleReturn.setTotalAmountTxnCurrency(finalTotal);
                saleReturn.setGrandTotalTxnCurrency(finalTotal);
                saleReturn.setDueAmountTxnCurrency(finalTotal.subtract(saleReturn.getPaidAmountTxnCurrency()));

                // Base currency conversion
                BigDecimal totalBase = finalTotal.multiply(exchangeRate);
                saleReturn.setTotalAmountBaseCurrency(totalBase);
                saleReturn.setDueAmountBaseCurrency(totalBase.subtract(saleReturn.getPaidAmountBaseCurrency()));

                SaleReturn saved = saleReturnRepository.save(saleReturn);
                return SaleReturnResponse.fromEntity(saved);
        }

        private void applyPromotionToSaleReturn(SaleReturn saleReturn, String couponCode,
                        Long currencyId, Long warehouseId,
                        Long companyId, Long customerId,
                        List<SaleReturnProduct> returnProducts) {
                // Build cart items from the return products
                List<CartItemDto> cartItems = returnProducts.stream()
                                .map(sp -> new CartItemDto(sp.getProduct().getId(), sp.getQuantity(),
                                                sp.getProductUnitPrice()))
                                .collect(Collectors.toList());

                CouponValidationRequest validationRequest = new CouponValidationRequest();
                validationRequest.setCouponCode(couponCode);
                validationRequest.setCustomerId(customerId);
                validationRequest.setWarehouseId(warehouseId);
                validationRequest.setCompanyId(companyId);
                validationRequest.setCurrencyCode(saleReturn.getCurrency().getCode()); // assuming currency loaded
                validationRequest.setItems(cartItems);
                validationRequest.setShippingCost(saleReturn.getShippingCost());

                CouponValidationResponse validation = promotionEngineService.validateCoupon(validationRequest);
                if (!validation.isValid()) {
                        throw new InvalidPromotionException(validation.getMessage());
                }

                if (validation.getAppliedPromotionId() != null) {
                        Promotion promo = promotionRepository.getReferenceById(validation.getAppliedPromotionId());
                        saleReturn.setAppliedPromotion(promo);
                }
                saleReturn.setPromotionDiscountAmount(validation.getDiscountAmount());
                saleReturn.setPromotionDiscountType(validation.getDiscountType());
                saleReturn.setPromotionCouponCode(couponCode);

                if (validation.isFreeShipping()) {
                        saleReturn.setShippingCost(BigDecimal.ZERO);
                }

                // Optionally record usage
                if (validation.getAppliedPromotionId() != null && saleReturn.getId() != null) {
                        promotionEngineService.recordPromotionUsage(validation.getAppliedPromotionId(),
                                        saleReturn.getId(), customerId, companyId);
                }
        }

        private BigDecimal recalculateSaleReturnTotal(SaleReturn saleReturn) {
                BigDecimal subtotal = saleReturn.getProducts().stream()
                                .map(SaleReturnProduct::getSubTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal afterManualDiscount = subtotal.subtract(saleReturn.getOrderDiscount());
                BigDecimal afterPromotion = afterManualDiscount.subtract(
                                saleReturn.getPromotionDiscountAmount() != null
                                                ? saleReturn.getPromotionDiscountAmount()
                                                : BigDecimal.ZERO);
                return afterPromotion
                                .add(saleReturn.getOrderTax())
                                .add(saleReturn.getShippingCost())
                                .add(saleReturn.getRoundingAmount());
        }

        @Override
        @Transactional(readOnly = true)
        public SaleReturnResponse getSaleReturnById(Long id) {
                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale return not found"));
                if (!saleReturn.getCompanyId().equals(UserContext.getCurrentCompanyId())) {
                        throw new SecurityException("Access denied");
                }
                return SaleReturnResponse.fromEntity(saleReturn);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleReturnResponse> getMySaleReturns() {
                Long currentUserId = UserContext.getCurrentUserId();
                return saleReturnRepository.findByCreatedBy(currentUserId).stream()
                                .map(SaleReturnResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleReturnResponse> getAllSaleReturns() {
                Long currentCompanyId = UserContext.getCurrentCompanyId();
                return saleReturnRepository.findByCompanyId(currentCompanyId).stream()
                                .map(SaleReturnResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public SaleReturnResponse updateSaleReturn(Long id, UpdateSaleReturnRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale return not found"));
                if (!saleReturn.getCompanyId().equals(currentCompanyId)) {
                        throw new SecurityException("Access denied");
                }

                // Restore stock for old products (since they were previously returned)
                Warehouse oldWarehouse = saleReturn.getWarehouse();
                for (SaleReturnProduct sp : saleReturn.getProducts()) {
                        productStockService.adjustStock(sp.getProduct().getId(), oldWarehouse.getId(),
                                        -sp.getQuantity());
                }

                // Update warehouse if changed
                if (request.getWarehouseId() != null && !request.getWarehouseId().equals(oldWarehouse.getId())) {
                        Warehouse newWarehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("New warehouse not found"));
                        saleReturn.setWarehouse(newWarehouse);
                        oldWarehouse = saleReturn.getWarehouse();
                }

                // Process products
                Set<Long> requestProductIds = request.getProducts() != null
                                ? request.getProducts().stream().map(
                                                UpdateSaleReturnRequest.SaleReturnProductUpdateRequest::getProductId)
                                                .collect(Collectors.toSet())
                                : new HashSet<>();

                List<SaleReturnProduct> updatedProducts = new ArrayList<>();
                BigDecimal subtotalSum = BigDecimal.ZERO;

                if (request.getProducts() != null) {
                        for (UpdateSaleReturnRequest.SaleReturnProductUpdateRequest p : request.getProducts()) {
                                Product product = productRepository.findById(p.getProductId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found: " + p.getProductId()));
                                int qty = Optional.ofNullable(p.getQuantity()).orElse(0);
                                if (qty <= 0)
                                        throw new RuntimeException("Invalid quantity for product: " + product.getId());

                                // return to stock again
                                productStockService.adjustStock(product.getId(), oldWarehouse.getId(), qty);

                                BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal discount = Optional.ofNullable(p.getDiscount()).orElse(BigDecimal.ZERO);
                                BigDecimal subTotal = p.getSubTotal() != null ? p.getSubTotal()
                                                : unitPrice.multiply(BigDecimal.valueOf(qty)).subtract(discount);
                                subtotalSum = subtotalSum.add(subTotal);

                                SaleReturnProduct existing = saleReturn.getProducts().stream()
                                                .filter(sp -> sp.getProduct().getId().equals(product.getId()))
                                                .findFirst().orElse(null);

                                if (existing != null) {
                                        existing.setQuantity(qty);
                                        existing.setProductUnitPrice(unitPrice);
                                        existing.setDiscount(discount);
                                        existing.setSubTotal(subTotal);
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
                                        SaleReturnProduct newSp = SaleReturnProduct.builder()
                                                        .saleReturn(saleReturn)
                                                        .product(product)
                                                        .productUnitPrice(unitPrice)
                                                        .quantity(qty)
                                                        .discount(discount)
                                                        .subTotal(subTotal)
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
                        saleReturn.getProducts().removeIf(sp -> !requestProductIds.contains(sp.getProduct().getId()));
                }
                saleReturn.setProducts(updatedProducts);

                // Update scalar fields
                Optional.ofNullable(request.getDate()).ifPresent(saleReturn::setDate);
                Optional.ofNullable(request.getOrderTax()).ifPresent(saleReturn::setOrderTax);
                Optional.ofNullable(request.getOrderDiscount()).ifPresent(saleReturn::setOrderDiscount);
                Optional.ofNullable(request.getOrderDiscountType()).ifPresent(saleReturn::setOrderDiscountType);
                Optional.ofNullable(request.getShippingCost()).ifPresent(saleReturn::setShippingCost);
                Optional.ofNullable(request.getRoundingAmount()).ifPresent(saleReturn::setRoundingAmount);
                Optional.ofNullable(request.getShipmentStatus()).ifPresent(saleReturn::setShipmentStatus);
                Optional.ofNullable(request.getSaleStatus()).ifPresent(saleReturn::setSaleStatus);
                Optional.ofNullable(request.getPaymentStatus()).ifPresent(saleReturn::setPaymentStatus);
                Optional.ofNullable(request.getSource()).ifPresent(saleReturn::setSource);
                Optional.ofNullable(request.getNote()).ifPresent(saleReturn::setNote);
                if (request.getCustomerId() != null) {
                        Customer customer = customerRepository.findById(request.getCustomerId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                        saleReturn.setCustomer(customer);
                }
                if (request.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(request.getCurrencyId())
                                        .orElseThrow(() -> new RuntimeException("Currency not found"));
                        saleReturn.setCurrency(currency);
                }
                if (request.getExchangeRate() != null) {
                        saleReturn.setExchangeRate(request.getExchangeRate());
                }

                // Promotion update (if a new coupon is provided)
                if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
                        // For simplicity, we reapply promotion based on current products
                        applyPromotionToSaleReturn(saleReturn, request.getCouponCode(),
                                        saleReturn.getCurrency().getId(), saleReturn.getWarehouse().getId(),
                                        currentCompanyId, saleReturn.getCustomer().getId(), saleReturn.getProducts());
                } else if (request.getCouponCode() != null && request.getCouponCode().isEmpty()) {
                        // If empty string, clear promotion
                        saleReturn.setAppliedPromotion(null);
                        saleReturn.setPromotionDiscountAmount(BigDecimal.ZERO);
                        saleReturn.setPromotionDiscountType(null);
                        saleReturn.setPromotionCouponCode(null);
                }
                // if couponCode is null, leave existing promotion untouched

                // Recalculate totals
                BigDecimal total = recalculateSaleReturnTotal(saleReturn);
                saleReturn.setTotalAmountTxnCurrency(total);
                saleReturn.setGrandTotalTxnCurrency(total);
                saleReturn.setDueAmountTxnCurrency(total.subtract(saleReturn.getPaidAmountTxnCurrency()));

                BigDecimal totalBase = total.multiply(saleReturn.getExchangeRate());
                saleReturn.setTotalAmountBaseCurrency(totalBase);
                saleReturn.setDueAmountBaseCurrency(totalBase.subtract(saleReturn.getPaidAmountBaseCurrency()));

                saleReturn.setUpdatedBy(currentUserId);
                saleReturn.setUpdatedAt(LocalDateTime.now());

                SaleReturn saved = saleReturnRepository.save(saleReturn);
                return SaleReturnResponse.fromEntity(saved);
        }

        @Override
        @Transactional
        public void deleteSaleReturn(Long id) {
                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale return not found"));
                if (!saleReturn.getCompanyId().equals(UserContext.getCurrentCompanyId())) {
                        throw new SecurityException("Access denied");
                }
                // Reverse stock adjustments (decrease stock back since the return is undone)
                for (SaleReturnProduct sp : saleReturn.getProducts()) {
                        productStockService.adjustStock(sp.getProduct().getId(), saleReturn.getWarehouse().getId(),
                                        -sp.getQuantity());
                }
                saleReturnRepository.delete(saleReturn);
        }
}