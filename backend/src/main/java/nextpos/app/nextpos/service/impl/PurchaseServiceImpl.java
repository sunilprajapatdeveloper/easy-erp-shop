package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePurchaseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseRequest;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.PurchaseResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductPrice;
import nextpos.app.nextpos.model.entity.Purchase;
import nextpos.app.nextpos.model.entity.PurchaseProduct;
import nextpos.app.nextpos.model.entity.Supplier;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.PurchaseRepository;
import nextpos.app.nextpos.repository.SupplierRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.PurchaseService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

        private final PurchaseRepository purchaseRepository;
        private final UserRepository userRepository;
        private final SupplierRepository supplierRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CurrencyRepository currencyRepository;
        private final ProductPriceRepository productPriceRepository;
        private final PaymentService paymentService;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public PurchaseResponse createPurchase(CreatePurchaseRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                // // company default currency
                // Currency currency = currencyRepository.findDefaultCurrency(user.getCompanyId())
                //                 .orElseThrow(() -> new RuntimeException("Company currency not configured"));

                Purchase purchase = Purchase.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("PURCHASE"))
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .supplier(supplier)
                                .warehouse(warehouse)
                                .note(request.getNote())
                                .orderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO))
                                .discount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO))
                                .shippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO))
                                .shippingStatus(Optional.ofNullable(request.getShippingStatus()).orElse(null))
                                .purchaseStatus(Optional.ofNullable(request.getPurchaseStatus()).orElse(null))
                                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                                // .currency(currency)
                                // .exchangeRate(Optional.ofNullable(currency.getExchangeRate()).orElse(BigDecimal.ONE))
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .companyId(user.getCompanyId())
                                .build();

                List<PurchaseProduct> purchaseProducts = new ArrayList<>();
                BigDecimal totalTxn = BigDecimal.ZERO;

                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = Optional.ofNullable(p.getPurchaseQty()).orElse(0);
                        if (qty <= 0) {
                                throw new RuntimeException("Invalid purchase quantity for product: " + product.getId());
                        }

                        // Instead of product.getCost(), use ProductPrice
                        // Here, you may need to get the companyId & warehouse to fetch correct
                        // ProductPrice
                        ProductPrice productPrice = productPriceRepository
                                        .findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                                        product.getId(),
                                                        warehouse.getId(),
                                                        null, // or channel if relevant
                                                        user.getCompanyId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "ProductPrice not found for product: " + product.getId()));

                        BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost())
                                        .orElse(productPrice.getCost());
                        BigDecimal prodDiscount = Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO);
                        BigDecimal prodTax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                        // Increase stock
                        productStockService.adjustStock(
                                        user.getCompanyId(),
                                        user.getId(),
                                        product.getId(),
                                        warehouse.getId(),
                                        qty);

                        // Optionally: update productPrice cost if you want to persist latest purchase
                        // cost
                        productPrice.setCost(unitCost);
                        productPriceRepository.save(productPrice);

                        // line subtotal
                        BigDecimal lineTotal = unitCost.multiply(BigDecimal.valueOf(qty))
                                        .subtract(prodDiscount)
                                        .add(prodTax);

                        totalTxn = totalTxn.add(lineTotal);

                        PurchaseProduct pp = PurchaseProduct.builder()
                                        .purchase(purchase)
                                        .product(product)
                                        .purchaseQty(qty)
                                        .productUnitCost(unitCost)
                                        .productDiscount(prodDiscount)
                                        .productTax(prodTax)
                                        .subTotal(lineTotal)
                                        .createdBy(user.getId())
                                        .createdAt(LocalDateTime.now())
                                        .companyId(user.getCompanyId())
                                        .build();

                        purchaseProducts.add(pp);
                }

                purchase.setProducts(purchaseProducts);

                // compute totals
                totalTxn = totalTxn.add(purchase.getOrderTax())
                                .subtract(purchase.getDiscount())
                                .add(purchase.getShippingCost());

                purchase.setTotalAmountTxnCurrency(totalTxn);
                purchase.setTotalAmountBaseCurrency(totalTxn.multiply(purchase.getExchangeRate()));

                Purchase saved = purchaseRepository.save(purchase);

                // handle optional inline payments
                List<PaymentResponse> paymentResponses = new ArrayList<>();
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.PURCHASE)
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

                                PaymentResponse paymentResponse = paymentService.createPayment(enriched);
                                paymentResponses.add(paymentResponse);
                        }
                }

                return new PurchaseResponse(saved, paymentResponses, productStockService);
        }

        @Override
        @Transactional(readOnly = true)
        public PurchaseResponse getPurchaseById(Long id) {
                Purchase purchase = purchaseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));

                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.PURCHASE,
                                purchase.getId());
                return new PurchaseResponse(purchase, payments, productStockService);
        }

        @Override
        @Transactional(readOnly = true)
        public List<PurchaseResponse> getMyPurchases() {
                User user = UserContext.getAuthenticatedUser(userRepository);

                List<Purchase> purchases = purchaseRepository.findByCreatedBy(user.getId());

                return purchases.stream()
                                .map(p -> {
                                        List<PaymentResponse> payments = paymentService
                                                        .getPaymentsByReference(PaymentSourceType.PURCHASE, p.getId());
                                        return new PurchaseResponse(p, payments, productStockService);
                                })
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<PurchaseResponse> getAllPurchases() {
                User user = UserContext.getAuthenticatedUser(userRepository);

                List<Purchase> purchases = purchaseRepository.findByCompanyId(user.getCompanyId());

                return purchases.stream()
                                .map(p -> {
                                        List<PaymentResponse> payments = paymentService
                                                        .getPaymentsByReference(PaymentSourceType.PURCHASE, p.getId());
                                        return new PurchaseResponse(p, payments, productStockService);
                                })
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public PurchaseResponse updatePurchase(Long id, UpdatePurchaseRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Purchase purchase = purchaseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));

                Warehouse warehouse = request.getWarehouseId() != null
                                ? warehouseRepository.findById(request.getWarehouseId())
                                                .orElseThrow(() -> new RuntimeException("Warehouse not found"))
                                : purchase.getWarehouse();

                // Rollback previous stock
                if (purchase.getProducts() != null && !purchase.getProducts().isEmpty()) {
                        for (PurchaseProduct old : purchase.getProducts()) {
                                productStockService.adjustStock(
                                                user.getCompanyId(),
                                                user.getId(),
                                                old.getProduct().getId(),
                                                purchase.getWarehouse().getId(),
                                                -old.getPurchaseQty() // remove previous quantity
                                );
                        }
                }

                List<PurchaseProduct> updatedProducts = new ArrayList<>();

                if (request.getProducts() != null && !request.getProducts().isEmpty()) {
                        for (var p : request.getProducts()) {
                                Product product = productRepository.findById(p.getProductId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found: " + p.getProductId()));

                                int qty = Optional.ofNullable(p.getPurchaseQty()).orElse(0);
                                if (qty <= 0) {
                                        throw new RuntimeException(
                                                        "Invalid purchase quantity for product: " + product.getId());
                                }

                                // Fetch ProductPrice for the product + warehouse
                                ProductPrice productPrice = productPriceRepository
                                                .findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                                                product.getId(),
                                                                warehouse.getId(),
                                                                null, // or channel if needed
                                                                user.getCompanyId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "ProductPrice not found for product: "
                                                                                + product.getId()));

                                BigDecimal unitCost = Optional.ofNullable(p.getProductUnitCost())
                                                .orElse(productPrice.getCost());
                                BigDecimal prodDiscount = Optional.ofNullable(p.getProductDiscount())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal prodTax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                                // Re-apply stock
                                productStockService.adjustStock(
                                                user.getCompanyId(),
                                                user.getId(),
                                                product.getId(),
                                                warehouse.getId(),
                                                qty);

                                // Update productPrice cost if needed
                                productPrice.setCost(unitCost);
                                productPriceRepository.save(productPrice);

                                // Compute line subtotal
                                BigDecimal lineTotal = unitCost.multiply(BigDecimal.valueOf(qty))
                                                .subtract(prodDiscount)
                                                .add(prodTax);

                                // Check if existing product line exists in purchase
                                PurchaseProduct existing = purchase.getProducts().stream()
                                                .filter(pp -> pp.getProduct() != null
                                                                && pp.getProduct().getId().equals(product.getId()))
                                                .findFirst()
                                                .orElse(null);

                                if (existing != null) {
                                        existing.setPurchaseQty(qty);
                                        existing.setProductUnitCost(unitCost);
                                        existing.setProductDiscount(prodDiscount);
                                        existing.setProductTax(prodTax);
                                        existing.setSubTotal(lineTotal);
                                        existing.setUpdatedBy(user.getId());
                                        existing.setUpdatedAt(LocalDateTime.now());
                                        updatedProducts.add(existing);
                                } else {
                                        PurchaseProduct pp = PurchaseProduct.builder()
                                                        .purchase(purchase)
                                                        .product(product)
                                                        .productUnitCost(unitCost)
                                                        .purchaseQty(qty)
                                                        .productDiscount(prodDiscount)
                                                        .productTax(prodTax)
                                                        .subTotal(lineTotal)
                                                        .createdBy(user.getId())
                                                        .createdAt(LocalDateTime.now())
                                                        .companyId(user.getCompanyId())
                                                        .build();
                                        updatedProducts.add(pp);
                                }
                        }
                }

                // Replace products (full replace)
                purchase.getProducts().clear();
                purchase.getProducts().addAll(updatedProducts);

                // Update purchase-level fields if provided
                Optional.ofNullable(request.getDate()).ifPresent(purchase::setDate);
                Optional.ofNullable(request.getOrderTax()).ifPresent(purchase::setOrderTax);
                Optional.ofNullable(request.getDiscount()).ifPresent(purchase::setDiscount);
                Optional.ofNullable(request.getShippingCost()).ifPresent(purchase::setShippingCost);
                Optional.ofNullable(request.getShippingStatus()).ifPresent(purchase::setShippingStatus);
                Optional.ofNullable(request.getPurchaseStatus()).ifPresent(purchase::setPurchaseStatus);
                Optional.ofNullable(request.getNote()).ifPresent(purchase::setNote);
                Optional.ofNullable(request.getExpectedDeliveryDate()).ifPresent(purchase::setExpectedDeliveryDate);

                if (request.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                        .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        purchase.setSupplier(supplier);
                }

                if (request.getWarehouseId() != null) {
                        purchase.setWarehouse(warehouse);
                }

                // Recompute totals
                computeAndSetTotals(purchase);

                purchase.setUpdatedBy(user.getId());
                purchase.setUpdatedAt(LocalDateTime.now());
                purchase.setCompanyId(user.getCompanyId());

                Purchase saved = purchaseRepository.save(purchase);

                // Handle payments appended in update
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.PURCHASE)
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

                // Reload payments
                List<PaymentResponse> payments = paymentService.getPaymentsByReference(
                                PaymentSourceType.PURCHASE, saved.getId());

                return new PurchaseResponse(saved, payments, productStockService);
        }

        @Override
        @Transactional
        public void deletePurchase(Long id) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Purchase purchase = purchaseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase not found"));

                Warehouse warehouse = purchase.getWarehouse();

                // Rollback stock for all products in this purchase
                if (purchase.getProducts() != null && !purchase.getProducts().isEmpty()) {
                        for (PurchaseProduct pp : purchase.getProducts()) {
                                productStockService.adjustStock(
                                                user.getCompanyId(),
                                                user.getId(),
                                                pp.getProduct().getId(),
                                                warehouse.getId(),
                                                -pp.getPurchaseQty() // decrease stock
                                );
                        }
                }

                // Delete purchase record
                purchaseRepository.delete(purchase);
        }

        // Helper: compute totals for a purchase and set on entity
        private void computeAndSetTotals(Purchase purchase) {
                BigDecimal subtotal = Optional.ofNullable(purchase.getProducts()).orElse(List.of()).stream()
                                .map(pp -> Optional.ofNullable(pp.getSubTotal()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalTxn = subtotal
                                .add(Optional.ofNullable(purchase.getOrderTax()).orElse(BigDecimal.ZERO))
                                .subtract(Optional.ofNullable(purchase.getDiscount()).orElse(BigDecimal.ZERO))
                                .add(Optional.ofNullable(purchase.getShippingCost()).orElse(BigDecimal.ZERO));

                purchase.setTotalAmountTxnCurrency(totalTxn);
                purchase.setTotalAmountBaseCurrency(totalTxn
                                .multiply(Optional.ofNullable(purchase.getExchangeRate()).orElse(BigDecimal.ONE)));
        }
}
