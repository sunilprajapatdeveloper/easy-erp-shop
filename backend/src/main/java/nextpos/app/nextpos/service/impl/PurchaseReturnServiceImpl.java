package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseReturnResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Purchase;
import nextpos.app.nextpos.model.entity.PurchaseProduct;
import nextpos.app.nextpos.model.entity.PurchaseReturn;
import nextpos.app.nextpos.model.entity.PurchaseReturnProduct;
import nextpos.app.nextpos.model.entity.Supplier;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.PurchaseRepository;
import nextpos.app.nextpos.repository.PurchaseReturnRepository;
import nextpos.app.nextpos.repository.SupplierRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PurchaseReturnService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseReturnServiceImpl implements PurchaseReturnService {

        private final PurchaseReturnRepository purchaseReturnRepository;
        private final PurchaseRepository purchaseRepository;
        private final ProductRepository productRepository;
        private final SupplierRepository supplierRepository;
        private final WarehouseRepository warehouseRepository;
        private final CurrencyRepository currencyRepository;
        private final UserRepository userRepository;
        private final PaymentService paymentService;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public PurchaseReturnResponse createPurchaseReturn(CreatePurchaseReturnRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                Purchase originalPurchase = purchaseRepository.findById(request.getOriginalPurchaseId())
                                .orElseThrow(() -> new RuntimeException("Original Purchase not found"));

                // Currency currency = currencyRepository.findDefaultCurrency(user.getCompanyId())
                //                 .orElseThrow(() -> new RuntimeException("Company currency not configured"));

                PurchaseReturn purchaseReturn = PurchaseReturn.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("PURCHASE-RETURN"))
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .originalPurchase(originalPurchase)
                                .supplier(supplier)
                                .warehouse(warehouse)
                                // .currency(currency)
                                .returnTax(nvl(request.getReturnTax()))
                                .returnDiscount(nvl(request.getReturnDiscount()))
                                .shippingCost(nvl(request.getShippingCost()))
                                // .exchangeRate(Optional.ofNullable(request.getExchangeRate())
                                //                 .orElse(currency.getExchangeRate()))
                                .shipmentStatus(request.getShipmentStatus())
                                .returnStatus(request.getReturnStatus())
                                .note(request.getNote())
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .companyId(user.getCompanyId())
                                .build();

                List<PurchaseReturnProduct> returnProducts = new ArrayList<>();
                BigDecimal subtotal = BigDecimal.ZERO;

                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        int requestedReturnQty = Optional.ofNullable(p.getReturnQty()).orElse(0);
                        if (requestedReturnQty <= 0) {
                                throw new RuntimeException(
                                                "Return quantity must be >= 1 for product: " + product.getId());
                        }

                        // total purchased for that product in original purchase
                        int purchasedQty = originalPurchase.getProducts().stream()
                                        .filter(pp -> pp.getProduct() != null
                                                        && pp.getProduct().getId().equals(product.getId()))
                                        .mapToInt(PurchaseProduct::getPurchaseQty)
                                        .sum();

                        // total already returned across other purchase returns
                        Integer alreadyReturnedAcrossAll = purchaseReturnRepository
                                        .sumReturnedQtyByPurchaseAndProduct(originalPurchase.getId(), product.getId());
                        int alreadyReturnedQty = alreadyReturnedAcrossAll == null ? 0 : alreadyReturnedAcrossAll;

                        int maxAllowedReturn = purchasedQty - alreadyReturnedQty;
                        if (requestedReturnQty > maxAllowedReturn) {
                                throw new RuntimeException("Return quantity for product "
                                                + product.getName() + " exceeds the purchased quantity. Max allowed: "
                                                + maxAllowedReturn);
                        }

                        // DECREASE stock atomically using ProductStockService
                        productStockService.adjustStock(
                                        user.getCompanyId(),
                                        user.getId(),
                                        product.getId(),
                                        warehouse.getId(),
                                        -requestedReturnQty);

                        BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost()).orElse(BigDecimal.ZERO);
                        BigDecimal discount = Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO);
                        BigDecimal tax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                        BigDecimal lineBase = unitCost.multiply(BigDecimal.valueOf(requestedReturnQty));
                        BigDecimal lineTotal = lineBase.subtract(discount).add(tax);
                        subtotal = subtotal.add(lineTotal);

                        PurchaseReturnProduct prp = PurchaseReturnProduct.builder()
                                        .purchaseReturn(purchaseReturn)
                                        .product(product)
                                        .returnQty(requestedReturnQty)
                                        .productUnitCost(unitCost)
                                        .productDiscount(discount)
                                        .productTax(tax)
                                        .createdBy(user.getId())
                                        .createdAt(LocalDateTime.now())
                                        .companyId(user.getCompanyId())
                                        .build();

                        returnProducts.add(prp);
                }

                purchaseReturn.setProducts(returnProducts);

                BigDecimal totalRefundTxn = subtotal
                                .add(Optional.ofNullable(purchaseReturn.getReturnTax()).orElse(BigDecimal.ZERO))
                                .subtract(Optional.ofNullable(purchaseReturn.getReturnDiscount())
                                                .orElse(BigDecimal.ZERO))
                                .add(Optional.ofNullable(purchaseReturn.getShippingCost()).orElse(BigDecimal.ZERO));

                purchaseReturn.setRefundAmountTxnCurrency(totalRefundTxn);
                purchaseReturn.setRefundAmountBaseCurrency(totalRefundTxn.multiply(purchaseReturn.getExchangeRate()));

                // persist purchase return
                PurchaseReturn saved = purchaseReturnRepository.save(purchaseReturn);

                // handle payments (refunds) if provided
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.PURCHASE_RETURN)
                                                .referenceId(saved.getId())
                                                .referenceNumber(saved.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(saved.getCurrency() != null
                                                                                ? saved.getCurrency().getCode()
                                                                                : null))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(saved.getExchangeRate()))
                                                .baseCurrencyAmount(paymentReq.getBaseCurrencyAmount())
                                                .paymentMethod(paymentReq.getPaymentMethod())
                                                .paymentData(paymentReq.getPaymentData())
                                                .status(paymentReq.getStatus())
                                                .paymentDate(paymentReq.getPaymentDate())
                                                .note(paymentReq.getNote())
                                                .transactionReference(paymentReq.getTransactionReference())
                                                .idempotencyKey(paymentReq.getIdempotencyKey())
                                                .build();

                                paymentService.createPayment(enriched);
                        }
                }

                return new PurchaseReturnResponse(saved);
        }

        @Override
        public PurchaseReturnResponse getPurchaseReturnById(Long id) {
                return purchaseReturnRepository.findById(id)
                                .map(PurchaseReturnResponse::new)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));
        }

        @Override
        @Transactional
        public PurchaseReturnResponse updatePurchaseReturn(Long id, UpdatePurchaseReturnRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                PurchaseReturn existing = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));

                Purchase originalPurchase = existing.getOriginalPurchase();

                // Build map of previous quantities for this return
                Map<Long, Integer> prevQtyMap = new HashMap<>();
                if (existing.getProducts() != null) {
                        for (PurchaseReturnProduct oldPr : existing.getProducts()) {
                                if (oldPr.getProduct() != null) {
                                        prevQtyMap.merge(oldPr.getProduct().getId(), oldPr.getReturnQty(),
                                                        Integer::sum);
                                }
                        }
                }

                // rollback previous stock using productStockService
                if (existing.getProducts() != null) {
                        for (PurchaseReturnProduct oldPr : existing.getProducts()) {
                                if (oldPr.getProduct() != null) {
                                        productStockService.adjustStock(
                                                        user.getCompanyId(),
                                                        user.getId(),
                                                        oldPr.getProduct().getId(),
                                                        existing.getWarehouse().getId(),
                                                        oldPr.getReturnQty() // add back previously returned qty
                                        );
                                }
                        }
                }

                existing.clearProducts();

                List<PurchaseReturnProduct> updatedProducts = new ArrayList<>();
                BigDecimal subtotal = BigDecimal.ZERO;

                if (request.getProducts() != null && !request.getProducts().isEmpty()) {
                        for (var p : request.getProducts()) {
                                Product product = productRepository.findById(p.getProductId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found with ID: " + p.getProductId()));

                                int requestedReturnQty = Optional.ofNullable(p.getReturnQty()).orElse(0);
                                if (requestedReturnQty <= 0) {
                                        throw new RuntimeException(
                                                        "Return quantity must be >= 1 for product: " + product.getId());
                                }

                                int purchasedQty = originalPurchase.getProducts().stream()
                                                .filter(pp -> pp.getProduct() != null
                                                                && pp.getProduct().getId().equals(product.getId()))
                                                .mapToInt(PurchaseProduct::getPurchaseQty)
                                                .sum();

                                Integer alreadyReturnedAcrossAll = purchaseReturnRepository
                                                .sumReturnedQtyByPurchaseAndProduct(originalPurchase.getId(),
                                                                product.getId());
                                int alreadyReturnedQty = alreadyReturnedAcrossAll == null ? 0
                                                : alreadyReturnedAcrossAll;

                                int prevQty = prevQtyMap.getOrDefault(product.getId(), 0);
                                int adjustedAlreadyReturned = Math.max(0, alreadyReturnedQty - prevQty);

                                int maxAllowedReturn = purchasedQty - adjustedAlreadyReturned;
                                if (requestedReturnQty > maxAllowedReturn) {
                                        throw new RuntimeException("Return quantity for product "
                                                        + product.getName() + " exceeds allowed quantity. Max allowed: "
                                                        + maxAllowedReturn);
                                }

                                // DECREASE stock using productStockService
                                productStockService.adjustStock(
                                                user.getCompanyId(),
                                                user.getId(),
                                                product.getId(),
                                                existing.getWarehouse().getId(),
                                                -requestedReturnQty);

                                BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal discount = Optional.ofNullable(p.getProductDiscount())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal tax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                                BigDecimal lineBase = unitCost.multiply(BigDecimal.valueOf(requestedReturnQty));
                                BigDecimal lineTotal = lineBase.subtract(discount).add(tax);
                                subtotal = subtotal.add(lineTotal);

                                PurchaseReturnProduct prp = PurchaseReturnProduct.builder()
                                                .purchaseReturn(existing)
                                                .product(product)
                                                .returnQty(requestedReturnQty)
                                                .productUnitCost(unitCost)
                                                .productDiscount(discount)
                                                .productTax(tax)
                                                .updatedBy(user.getId())
                                                .updatedAt(LocalDateTime.now())
                                                .companyId(user.getCompanyId())
                                                .build();

                                updatedProducts.add(prp);
                        }

                        existing.setProducts(updatedProducts);
                }

                // Update fields if provided
                Optional.ofNullable(request.getDate()).ifPresent(existing::setDate);
                Optional.ofNullable(request.getReturnTax()).ifPresent(existing::setReturnTax);
                Optional.ofNullable(request.getReturnDiscount()).ifPresent(existing::setReturnDiscount);
                Optional.ofNullable(request.getShippingCost()).ifPresent(existing::setShippingCost);
                Optional.ofNullable(request.getExchangeRate()).ifPresent(existing::setExchangeRate);
                Optional.ofNullable(request.getShipmentStatus()).ifPresent(existing::setShipmentStatus);
                Optional.ofNullable(request.getReturnStatus()).ifPresent(existing::setReturnStatus);
                Optional.ofNullable(request.getNote()).ifPresent(existing::setNote);

                if (request.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                        .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        existing.setSupplier(supplier);
                }

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        existing.setWarehouse(warehouse);
                }

                if (request.getOriginalPurchaseId() != null) {
                        Purchase purchase = purchaseRepository.findById(request.getOriginalPurchaseId())
                                        .orElseThrow(() -> new RuntimeException("Original Purchase not found"));
                        existing.setOriginalPurchase(purchase);
                }

                // Compute subtotal
                BigDecimal computedSubtotal = subtotal;
                if (request.getProducts() == null || request.getProducts().isEmpty()) {
                        computedSubtotal = Optional.ofNullable(existing.getProducts()).orElse(List.of()).stream()
                                        .map(prp -> {
                                                BigDecimal u = Optional.ofNullable(prp.getProductUnitCost())
                                                                .orElse(BigDecimal.ZERO);
                                                BigDecimal d = Optional.ofNullable(prp.getProductDiscount())
                                                                .orElse(BigDecimal.ZERO);
                                                BigDecimal t = Optional.ofNullable(prp.getProductTax())
                                                                .orElse(BigDecimal.ZERO);
                                                int q = Optional.ofNullable(prp.getReturnQty()).orElse(0);
                                                return u.multiply(BigDecimal.valueOf(q)).subtract(d).add(t);
                                        })
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                }

                BigDecimal totalRefundTxn = computedSubtotal
                                .add(Optional.ofNullable(existing.getReturnTax()).orElse(BigDecimal.ZERO))
                                .subtract(Optional.ofNullable(existing.getReturnDiscount()).orElse(BigDecimal.ZERO))
                                .add(Optional.ofNullable(existing.getShippingCost()).orElse(BigDecimal.ZERO));

                existing.setRefundAmountTxnCurrency(totalRefundTxn);
                existing.setRefundAmountBaseCurrency(totalRefundTxn
                                .multiply(Optional.ofNullable(existing.getExchangeRate()).orElse(BigDecimal.ONE)));

                existing.setUpdatedBy(user.getId());
                existing.setUpdatedAt(LocalDateTime.now());
                existing.setCompanyId(user.getCompanyId());

                PurchaseReturn saved = purchaseReturnRepository.save(existing);

                // handle appended payments
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.PURCHASE_RETURN)
                                                .referenceId(saved.getId())
                                                .referenceNumber(saved.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(saved.getCurrency() != null
                                                                                ? saved.getCurrency().getCode()
                                                                                : null))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(saved.getExchangeRate()))
                                                .baseCurrencyAmount(paymentReq.getBaseCurrencyAmount())
                                                .paymentMethod(paymentReq.getPaymentMethod())
                                                .paymentData(paymentReq.getPaymentData())
                                                .status(paymentReq.getStatus())
                                                .paymentDate(paymentReq.getPaymentDate())
                                                .note(paymentReq.getNote())
                                                .transactionReference(paymentReq.getTransactionReference())
                                                .idempotencyKey(paymentReq.getIdempotencyKey())
                                                .build();

                                paymentService.createPayment(enriched);
                        }
                }

                return new PurchaseReturnResponse(saved);
        }

        @Override
        public List<PurchaseReturnResponse> getAllPurchaseReturns(Long companyId, Long supplierId, Long warehouseId) {
                return purchaseReturnRepository.findAll().stream()
                                .filter(pr -> pr.getCompanyId().equals(companyId))
                                .filter(pr -> supplierId == null || pr.getSupplier().getId().equals(supplierId))
                                .filter(pr -> warehouseId == null || pr.getWarehouse().getId().equals(warehouseId))
                                .map(PurchaseReturnResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void deletePurchaseReturn(Long id) {
                PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));

                // restore stock (since the return previously decreased stock)
                rollbackStock(purchaseReturn.getProducts());
                purchaseReturnRepository.delete(purchaseReturn);
        }

        // Helpers
        private void rollbackStock(List<PurchaseReturnProduct> products) {
                if (products == null || products.isEmpty()) {
                        return;
                }

                User user = UserContext.getAuthenticatedUser(userRepository);

                for (PurchaseReturnProduct prp : products) {
                        Product product = prp.getProduct();
                        if (product != null && prp.getPurchaseReturn() != null) {
                                Warehouse warehouse = prp.getPurchaseReturn().getWarehouse();
                                if (warehouse != null) {
                                        // Add back the returned quantity
                                        productStockService.adjustStock(
                                                        user.getCompanyId(),
                                                        user.getId(),
                                                        product.getId(),
                                                        warehouse.getId(),
                                                        prp.getReturnQty() // restore stock
                                        );
                                }
                        }
                }
        }

        private BigDecimal nvl(BigDecimal val) {
                return val == null ? BigDecimal.ZERO : val;
        }
}
