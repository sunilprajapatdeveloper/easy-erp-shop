package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePurchaseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.enums.*;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PurchaseService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

        private final PurchaseRepository purchaseRepository;
        private final SupplierRepository supplierRepository;
        private final ProductRepository productRepository;
        private final CurrencyRepository currencyRepository;
        private final ProductPriceRepository productPriceRepository;
        private final ProductStockService productStockService;
        private final WarehouseAccessService warehouseAccessService;

        @Override
        @Transactional
        public PurchaseResponse createPurchase(CreatePurchaseRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Supplier supplier = supplierRepository.findByIdAndCompanyId(request.getSupplierId(), currentCompanyId)
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));
                Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new RuntimeException("Currency not found"));
                BigDecimal exchangeRate = request.getExchangeRate();
                if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("Valid exchange rate is required");
                }

                Purchase purchase = Purchase.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("PURCHASE"))
                                .invoiceNumber(request.getInvoiceNumber())
                                .receiptNumber(request.getReceiptNumber())
                                .supplierInvoiceNumber(request.getSupplierInvoiceNumber())
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .supplier(supplier)
                                .warehouse(warehouse)
                                .currency(currency)
                                .exchangeRate(exchangeRate)
                                .note(request.getNote())
                                .orderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO))
                                .orderDiscount(Optional.ofNullable(request.getOrderDiscount()).orElse(BigDecimal.ZERO))
                                .orderDiscountType(request.getOrderDiscountType())
                                .shippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO))
                                .roundingAmount(Optional.ofNullable(request.getRoundingAmount())
                                                .orElse(BigDecimal.ZERO))
                                .shippingStatus(Optional.ofNullable(request.getShippingStatus())
                                                .orElse(ShipmentStatus.PENDING))
                                .purchaseStatus(Optional.ofNullable(request.getPurchaseStatus())
                                                .orElse(PurchaseStatus.PENDING))
                                .paymentStatus(Optional.ofNullable(request.getPaymentStatus())
                                                .orElse(PaymentStatus.PENDING))
                                .source(Optional.ofNullable(request.getSource()).orElse(PurchaseSource.MANUAL))
                                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                                .createdBy(currentUserId)
                                .createdAt(LocalDateTime.now())
                                .companyId(currentCompanyId)
                                .build();

                BigDecimal subtotalSum = BigDecimal.ZERO;
                List<PurchaseProduct> purchaseProducts = new ArrayList<>();

                for (CreatePurchaseRequest.PurchaseProductRequest p : request.getProducts()) {
                        Product product = productRepository
                                        .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), currentCompanyId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = p.getQuantity();
                        if (qty <= 0)
                                throw new RuntimeException("Invalid quantity for product: " + product.getId());

                        // Optionally update cost in ProductPrice
                        productPriceRepository.findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                        product.getId(), warehouse.getId(), null, currentCompanyId)
                                        .ifPresent(price -> {
                                                price.setCost(p.getProductUnitCost());
                                                productPriceRepository.save(price);
                                        });

                        // Increase stock
                        productStockService.adjustStock(product.getId(), warehouse.getId(), qty);

                        BigDecimal unitCost = p.getProductUnitCost();
                        BigDecimal discount = p.getDiscount();
                        BigDecimal subTotal = p.getSubTotal();
                        subtotalSum = subtotalSum.add(subTotal);

                        PurchaseProduct pp = PurchaseProduct.builder()
                                        .purchase(purchase)
                                        .product(product)
                                        .productUnitCost(unitCost)
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
                        purchaseProducts.add(pp);
                }
                purchase.setProducts(purchaseProducts);

                // Order‑level totals
                BigDecimal total = subtotalSum
                                .add(purchase.getOrderTax())
                                .subtract(purchase.getOrderDiscount())
                                .add(purchase.getShippingCost())
                                .add(purchase.getRoundingAmount());

                purchase.setTotalAmountTxnCurrency(total);
                purchase.setGrandTotalTxnCurrency(total);
                purchase.setPaidAmountTxnCurrency(BigDecimal.ZERO);
                purchase.setDueAmountTxnCurrency(total);

                BigDecimal totalBase = total.multiply(exchangeRate);
                purchase.setTotalAmountBaseCurrency(totalBase);
                purchase.setPaidAmountBaseCurrency(BigDecimal.ZERO);
                purchase.setDueAmountBaseCurrency(totalBase);

                Purchase saved = purchaseRepository.save(purchase);
                return PurchaseResponse.fromEntity(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public PurchaseResponse getPurchaseById(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                Purchase purchase = purchaseRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));
                warehouseAccessService.requireAccessible(purchase.getWarehouse().getId());
                return PurchaseResponse.fromEntity(purchase);
        }

        @Override
        public List<PurchaseResponse> getMyPurchases() {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();
                List<Long> warehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                                .map(Warehouse::getId).toList();
                return purchaseRepository
                                .findByCreatedByAndCompanyIdAndWarehouse_IdIn(currentUserId, companyId, warehouseIds)
                                .stream()
                                .map(PurchaseResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        public List<PurchaseResponse> getAllPurchases() {
                Long currentCompanyId = UserContext.getCurrentCompanyId();
                List<Long> warehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                                .map(Warehouse::getId).toList();
                return purchaseRepository.findByCompanyIdAndWarehouse_IdIn(currentCompanyId, warehouseIds).stream()
                                .map(PurchaseResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public PurchaseResponse updatePurchase(Long id, UpdatePurchaseRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Purchase purchase = purchaseRepository.findByIdAndCompanyId(id, currentCompanyId)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));

                // Rollback old stock
                Warehouse warehouse = purchase.getWarehouse();
                warehouseAccessService.requireAccessible(warehouse.getId());
                for (PurchaseProduct pp : purchase.getProducts()) {
                        productStockService.adjustStock(pp.getProduct().getId(), warehouse.getId(), -pp.getQuantity());
                }

                // Update warehouse if changed
                if (request.getWarehouseId() != null && !request.getWarehouseId().equals(warehouse.getId())) {
                        Warehouse newWarehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                        purchase.setWarehouse(newWarehouse);
                        warehouse = newWarehouse;
                }

                Set<Long> requestProductIds = request.getProducts() != null
                                ? request.getProducts().stream()
                                                .map(UpdatePurchaseRequest.PurchaseProductRequest::getProductId)
                                                .collect(Collectors.toSet())
                                : new HashSet<>();

                List<PurchaseProduct> updatedProducts = new ArrayList<>();
                BigDecimal subtotalSum = BigDecimal.ZERO;

                if (request.getProducts() != null) {
                        for (UpdatePurchaseRequest.PurchaseProductRequest p : request.getProducts()) {
                                Product product = productRepository
                                                .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), currentCompanyId)
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found: " + p.getProductId()));
                                int qty = Optional.ofNullable(p.getQuantity()).orElse(0);
                                if (qty <= 0)
                                        throw new RuntimeException("Invalid quantity for product: " + product.getId());

                                // Update cost in ProductPrice if exists
                                productPriceRepository.findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                                product.getId(), warehouse.getId(), null, currentCompanyId)
                                                .ifPresent(price -> {
                                                        price.setCost(p.getProductUnitCost());
                                                        productPriceRepository.save(price);
                                                });

                                productStockService.adjustStock(product.getId(), warehouse.getId(), qty);

                                BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal discount = Optional.ofNullable(p.getDiscount()).orElse(BigDecimal.ZERO);
                                BigDecimal subTotal = p.getSubTotal() != null ? p.getSubTotal()
                                                : unitCost.multiply(BigDecimal.valueOf(qty)).subtract(discount);
                                subtotalSum = subtotalSum.add(subTotal);

                                PurchaseProduct existing = purchase.getProducts().stream()
                                                .filter(pp -> pp.getProduct().getId().equals(product.getId()))
                                                .findFirst().orElse(null);

                                if (existing != null) {
                                        existing.setQuantity(qty);
                                        existing.setProductUnitCost(unitCost);
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
                                        PurchaseProduct newPp = PurchaseProduct.builder()
                                                        .purchase(purchase)
                                                        .product(product)
                                                        .productUnitCost(unitCost)
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
                                        updatedProducts.add(newPp);
                                }
                        }
                }

                if (!requestProductIds.isEmpty()) {
                        purchase.getProducts().removeIf(pp -> !requestProductIds.contains(pp.getProduct().getId()));
                }
                purchase.setProducts(updatedProducts);

                // Update scalar fields
                Optional.ofNullable(request.getDate()).ifPresent(purchase::setDate);
                Optional.ofNullable(request.getOrderTax()).ifPresent(purchase::setOrderTax);
                Optional.ofNullable(request.getOrderDiscount()).ifPresent(purchase::setOrderDiscount);
                Optional.ofNullable(request.getOrderDiscountType()).ifPresent(purchase::setOrderDiscountType);
                Optional.ofNullable(request.getShippingCost()).ifPresent(purchase::setShippingCost);
                Optional.ofNullable(request.getRoundingAmount()).ifPresent(purchase::setRoundingAmount);
                Optional.ofNullable(request.getShippingStatus()).ifPresent(purchase::setShippingStatus);
                Optional.ofNullable(request.getPurchaseStatus()).ifPresent(purchase::setPurchaseStatus);
                Optional.ofNullable(request.getPaymentStatus()).ifPresent(purchase::setPaymentStatus);
                Optional.ofNullable(request.getInvoiceNumber()).ifPresent(purchase::setInvoiceNumber);
                Optional.ofNullable(request.getReceiptNumber()).ifPresent(purchase::setReceiptNumber); // added
                Optional.ofNullable(request.getSupplierInvoiceNumber()).ifPresent(purchase::setSupplierInvoiceNumber); // added
                Optional.ofNullable(request.getSource()).ifPresent(purchase::setSource);
                Optional.ofNullable(request.getNote()).ifPresent(purchase::setNote);
                Optional.ofNullable(request.getExpectedDeliveryDate()).ifPresent(purchase::setExpectedDeliveryDate);
                if (request.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findByIdAndCompanyId(request.getSupplierId(), currentCompanyId)
                                        .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        purchase.setSupplier(supplier);
                }
                if (request.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(request.getCurrencyId())
                                        .orElseThrow(() -> new RuntimeException("Currency not found"));
                        purchase.setCurrency(currency);
                }
                if (request.getExchangeRate() != null) {
                        purchase.setExchangeRate(request.getExchangeRate());
                }

                BigDecimal total = subtotalSum
                                .add(purchase.getOrderTax())
                                .subtract(purchase.getOrderDiscount())
                                .add(purchase.getShippingCost())
                                .add(purchase.getRoundingAmount());

                purchase.setTotalAmountTxnCurrency(total);
                purchase.setGrandTotalTxnCurrency(total);
                purchase.setDueAmountTxnCurrency(total.subtract(purchase.getPaidAmountTxnCurrency()));

                BigDecimal totalBase = total.multiply(purchase.getExchangeRate());
                purchase.setTotalAmountBaseCurrency(totalBase);
                purchase.setDueAmountBaseCurrency(totalBase.subtract(purchase.getPaidAmountBaseCurrency()));

                purchase.setUpdatedBy(currentUserId);
                purchase.setUpdatedAt(LocalDateTime.now());

                Purchase saved = purchaseRepository.save(purchase);
                return PurchaseResponse.fromEntity(saved);
        }

        @Override
        @Transactional
        public void deletePurchase(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                Purchase purchase = purchaseRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));

                Warehouse warehouse = purchase.getWarehouse();
                warehouseAccessService.requireAccessible(warehouse.getId());
                for (PurchaseProduct pp : purchase.getProducts()) {
                        productStockService.adjustStock(pp.getProduct().getId(), warehouse.getId(), -pp.getQuantity());
                }
                purchaseRepository.delete(purchase);
        }
}
