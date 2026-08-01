package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseReturnResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.enums.*;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PurchaseReturnService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseReturnServiceImpl implements PurchaseReturnService {

        private final PurchaseReturnRepository purchaseReturnRepository;
        private final PurchaseRepository purchaseRepository;
        private final SupplierRepository supplierRepository;
        private final ProductRepository productRepository;
        private final CurrencyRepository currencyRepository;
        private final ProductStockService productStockService;
        private final WarehouseAccessService warehouseAccessService;

        @Override
        @Transactional
        public PurchaseReturnResponse createPurchaseReturn(CreatePurchaseReturnRequest request) {
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

                Purchase originalPurchase = purchaseRepository
                                .findByIdAndCompanyId(request.getOriginalPurchaseId(), currentCompanyId)
                                .orElseThrow(() -> new RuntimeException("Original purchase not found"));
                warehouseAccessService.requireAccessible(originalPurchase.getWarehouse().getId());

                PurchaseReturn purchaseReturn = PurchaseReturn.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("PURCHASE-RETURN"))
                                .invoiceNumber(request.getInvoiceNumber()) // added
                                .receiptNumber(request.getReceiptNumber()) // added
                                .supplierInvoiceNumber(request.getSupplierInvoiceNumber()) // added
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .originalPurchase(originalPurchase)
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
                                .companyId(currentCompanyId)
                                .createdBy(currentUserId)
                                .createdAt(LocalDateTime.now())
                                .build();

                BigDecimal subtotalSum = BigDecimal.ZERO;
                List<PurchaseReturnProduct> returnProducts = new ArrayList<>();

                for (CreatePurchaseReturnRequest.PurchaseReturnProductRequest p : request.getProducts()) {
                        Product product = productRepository
                                        .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), currentCompanyId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = p.getQuantity();
                        if (qty <= 0)
                                throw new RuntimeException("Invalid quantity for product: " + product.getId());

                        // Check against original purchase quantity minus already returned qty
                        int purchasedQty = originalPurchase.getProducts().stream()
                                        .filter(pp -> pp.getProduct().getId().equals(product.getId()))
                                        .mapToInt(PurchaseProduct::getQuantity)
                                        .sum();
                        int alreadyReturned = purchaseReturnRepository.sumReturnedQtyByPurchaseAndProduct(
                                        originalPurchase.getId(), product.getId(), currentCompanyId);
                        if (qty > purchasedQty - alreadyReturned) {
                                throw new RuntimeException("Return quantity exceeds purchased quantity for product: "
                                                + product.getName());
                        }

                        // Return to supplier => decrease stock
                        productStockService.adjustStock(product.getId(), warehouse.getId(), -qty);

                        BigDecimal unitCost = p.getProductUnitCost();
                        BigDecimal discount = p.getDiscount();
                        BigDecimal subTotal = p.getSubTotal();
                        subtotalSum = subtotalSum.add(subTotal);

                        PurchaseReturnProduct prp = PurchaseReturnProduct.builder()
                                        .purchaseReturn(purchaseReturn)
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
                        returnProducts.add(prp);
                }

                purchaseReturn.setProducts(returnProducts);

                BigDecimal total = subtotalSum
                                .add(purchaseReturn.getOrderTax())
                                .subtract(purchaseReturn.getOrderDiscount())
                                .add(purchaseReturn.getShippingCost())
                                .add(purchaseReturn.getRoundingAmount());

                purchaseReturn.setTotalAmountTxnCurrency(total);
                purchaseReturn.setGrandTotalTxnCurrency(total);
                purchaseReturn.setPaidAmountTxnCurrency(BigDecimal.ZERO);
                purchaseReturn.setDueAmountTxnCurrency(total);

                BigDecimal totalBase = total.multiply(exchangeRate);
                purchaseReturn.setTotalAmountBaseCurrency(totalBase);
                purchaseReturn.setPaidAmountBaseCurrency(BigDecimal.ZERO);
                purchaseReturn.setDueAmountBaseCurrency(totalBase);

                PurchaseReturn saved = purchaseReturnRepository.save(purchaseReturn);
                return PurchaseReturnResponse.fromEntity(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public PurchaseReturnResponse getPurchaseReturnById(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                PurchaseReturn pr = purchaseReturnRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Purchase return not found"));
                warehouseAccessService.requireAccessible(pr.getWarehouse().getId());
                return PurchaseReturnResponse.fromEntity(pr);
        }

        @Override
        public List<PurchaseReturnResponse> getMyPurchaseReturns() {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();
                List<Long> warehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                                .map(Warehouse::getId).toList();
                return purchaseReturnRepository
                                .findByCreatedByAndCompanyIdAndWarehouse_IdIn(currentUserId, companyId, warehouseIds)
                                .stream()
                                .map(PurchaseReturnResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        public List<PurchaseReturnResponse> getAllPurchaseReturns() {
                Long currentCompanyId = UserContext.getCurrentCompanyId();
                List<Long> warehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                                .map(Warehouse::getId).toList();
                return purchaseReturnRepository.findByCompanyIdAndWarehouse_IdIn(currentCompanyId, warehouseIds).stream()
                                .map(PurchaseReturnResponse::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public PurchaseReturnResponse updatePurchaseReturn(Long id, UpdatePurchaseReturnRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                PurchaseReturn pr = purchaseReturnRepository.findByIdAndCompanyId(id, currentCompanyId)
                                .orElseThrow(() -> new RuntimeException("Purchase return not found"));

                Purchase originalPurchase = pr.getOriginalPurchase();
                Warehouse warehouse = pr.getWarehouse();
                warehouseAccessService.requireAccessible(warehouse.getId());

                // Restore stock for previously returned products
                for (PurchaseReturnProduct oldPr : pr.getProducts()) {
                        productStockService.adjustStock(oldPr.getProduct().getId(), warehouse.getId(),
                                        oldPr.getQuantity());
                }

                // Update warehouse if changed
                if (request.getWarehouseId() != null && !request.getWarehouseId().equals(warehouse.getId())) {
                        Warehouse newWarehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                        pr.setWarehouse(newWarehouse);
                        warehouse = newWarehouse;
                }

                Set<Long> requestProductIds = request.getProducts() != null
                                ? request.getProducts().stream()
                                                .map(UpdatePurchaseReturnRequest.PurchaseReturnProductRequest::getProductId)
                                                .collect(Collectors.toSet())
                                : new HashSet<>();

                List<PurchaseReturnProduct> updatedProducts = new ArrayList<>();
                BigDecimal subtotalSum = BigDecimal.ZERO;

                if (request.getProducts() != null) {
                        for (UpdatePurchaseReturnRequest.PurchaseReturnProductRequest p : request.getProducts()) {
                                Product product = productRepository
                                                .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), currentCompanyId)
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found: " + p.getProductId()));

                                int qty = Optional.ofNullable(p.getQuantity()).orElse(0);
                                if (qty <= 0)
                                        throw new RuntimeException("Invalid quantity for product: " + product.getId());

                                // Check return limit
                                int purchasedQty = originalPurchase.getProducts().stream()
                                                .filter(pp -> pp.getProduct().getId().equals(product.getId()))
                                                .mapToInt(PurchaseProduct::getQuantity)
                                                .sum();
                                int alreadyReturned = purchaseReturnRepository.sumReturnedQtyByPurchaseAndProduct(
                                                originalPurchase.getId(), product.getId(), currentCompanyId);
                                int oldQty = pr.getProducts().stream()
                                                .filter(pp -> pp.getProduct().getId().equals(product.getId()))
                                                .mapToInt(PurchaseReturnProduct::getQuantity)
                                                .sum();
                                int adjustedAlready = alreadyReturned - oldQty;
                                if (qty > purchasedQty - adjustedAlready) {
                                        throw new RuntimeException(
                                                        "Return quantity exceeds purchased quantity for product: "
                                                                        + product.getName());
                                }

                                productStockService.adjustStock(product.getId(), warehouse.getId(), -qty);

                                BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal discount = Optional.ofNullable(p.getDiscount()).orElse(BigDecimal.ZERO);
                                BigDecimal subTotal = p.getSubTotal() != null ? p.getSubTotal()
                                                : unitCost.multiply(BigDecimal.valueOf(qty)).subtract(discount);
                                subtotalSum = subtotalSum.add(subTotal);

                                PurchaseReturnProduct existing = pr.getProducts().stream()
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
                                        PurchaseReturnProduct newPrp = PurchaseReturnProduct.builder()
                                                        .purchaseReturn(pr)
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
                                        updatedProducts.add(newPrp);
                                }
                        }
                }

                if (!requestProductIds.isEmpty()) {
                        pr.getProducts().removeIf(pp -> !requestProductIds.contains(pp.getProduct().getId()));
                }
                pr.setProducts(updatedProducts);

                // Update scalar fields
                Optional.ofNullable(request.getDate()).ifPresent(pr::setDate);
                Optional.ofNullable(request.getOrderTax()).ifPresent(pr::setOrderTax);
                Optional.ofNullable(request.getOrderDiscount()).ifPresent(pr::setOrderDiscount);
                Optional.ofNullable(request.getOrderDiscountType()).ifPresent(pr::setOrderDiscountType);
                Optional.ofNullable(request.getShippingCost()).ifPresent(pr::setShippingCost);
                Optional.ofNullable(request.getRoundingAmount()).ifPresent(pr::setRoundingAmount);
                Optional.ofNullable(request.getShippingStatus()).ifPresent(pr::setShippingStatus);
                Optional.ofNullable(request.getPurchaseStatus()).ifPresent(pr::setPurchaseStatus);
                Optional.ofNullable(request.getPaymentStatus()).ifPresent(pr::setPaymentStatus);
                Optional.ofNullable(request.getInvoiceNumber()).ifPresent(pr::setInvoiceNumber); // added
                Optional.ofNullable(request.getReceiptNumber()).ifPresent(pr::setReceiptNumber); // added
                Optional.ofNullable(request.getSupplierInvoiceNumber()).ifPresent(pr::setSupplierInvoiceNumber);// added
                Optional.ofNullable(request.getSource()).ifPresent(pr::setSource);
                Optional.ofNullable(request.getNote()).ifPresent(pr::setNote);
                if (request.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findByIdAndCompanyId(request.getSupplierId(), currentCompanyId)
                                        .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        pr.setSupplier(supplier);
                }
                if (request.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(request.getCurrencyId())
                                        .orElseThrow(() -> new RuntimeException("Currency not found"));
                        pr.setCurrency(currency);
                }
                if (request.getExchangeRate() != null) {
                        pr.setExchangeRate(request.getExchangeRate());
                }
                if (request.getOriginalPurchaseId() != null) {
                        Purchase newPurchase = purchaseRepository
                                        .findByIdAndCompanyId(request.getOriginalPurchaseId(), currentCompanyId)
                                        .orElseThrow(() -> new RuntimeException("Original purchase not found"));
                        warehouseAccessService.requireAccessible(newPurchase.getWarehouse().getId());
                        pr.setOriginalPurchase(newPurchase);
                }

                BigDecimal total = subtotalSum
                                .add(pr.getOrderTax())
                                .subtract(pr.getOrderDiscount())
                                .add(pr.getShippingCost())
                                .add(pr.getRoundingAmount());

                pr.setTotalAmountTxnCurrency(total);
                pr.setGrandTotalTxnCurrency(total);
                pr.setDueAmountTxnCurrency(total.subtract(pr.getPaidAmountTxnCurrency()));

                BigDecimal totalBase = total.multiply(pr.getExchangeRate());
                pr.setTotalAmountBaseCurrency(totalBase);
                pr.setDueAmountBaseCurrency(totalBase.subtract(pr.getPaidAmountBaseCurrency()));

                pr.setUpdatedBy(currentUserId);
                pr.setUpdatedAt(LocalDateTime.now());

                PurchaseReturn saved = purchaseReturnRepository.save(pr);
                return PurchaseReturnResponse.fromEntity(saved);
        }

        @Override
        @Transactional
        public void deletePurchaseReturn(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                PurchaseReturn pr = purchaseReturnRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Purchase return not found"));
                warehouseAccessService.requireAccessible(pr.getWarehouse().getId());
                for (PurchaseReturnProduct prp : pr.getProducts()) {
                        productStockService.adjustStock(prp.getProduct().getId(), pr.getWarehouse().getId(),
                                        prp.getQuantity());
                }
                purchaseReturnRepository.delete(pr);
        }
}
