package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.CreateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.SaleReturnResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductPrice;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleReturn;
import nextpos.app.nextpos.model.entity.SaleReturnProduct;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.SaleRepository;
import nextpos.app.nextpos.repository.SaleReturnRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.SaleReturnService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class SaleReturnServiceImpl implements SaleReturnService {

        private final SaleReturnRepository saleReturnRepository;
        private final UserRepository userRepository;
        private final CustomerRepository customerRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final SaleRepository saleRepository;
        private final CurrencyRepository currencyRepository;
        private final ProductPriceRepository productPriceRepository;
        private final PaymentService paymentService;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public SaleReturnResponse createSaleReturn(CreateSaleReturnRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                Sale originalSale = saleRepository.findById(request.getOriginalSaleId())
                                .orElseThrow(() -> new RuntimeException("Original Sale not found"));

                // Currency currency = currencyRepository.findDefaultCurrency(user.getCompanyId())
                //                 .orElseThrow(() -> new RuntimeException("Company currency not configured"));

                SaleReturn saleReturn = SaleReturn.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("SALE-RETURN"))
                                .date(Optional.ofNullable(request.getDate()).orElse(java.time.LocalDate.now()))
                                .originalSale(originalSale)
                                .customer(customer)
                                .warehouse(warehouse)
                                .returnTax(nvl(request.getReturnTax()))
                                .returnDiscount(nvl(request.getReturnDiscount()))
                                .shippingCost(nvl(request.getShippingCost()))
                                .shipmentStatus(Optional.ofNullable(request.getShipmentStatus())
                                                .orElse(ShipmentStatus.PENDING))
                                .returnStatus(Optional.ofNullable(request.getReturnStatus()).orElse(SaleStatus.PENDING))
                                .note(request.getNote())
                                // .currency(currency)
                                // .exchangeRate(Optional.ofNullable(currency.getExchangeRate()).orElse(BigDecimal.ONE))
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .companyId(user.getCompanyId())
                                .build();

                List<SaleReturnProduct> returnProducts = new ArrayList<>();

                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        int requestedReturnQty = Optional.ofNullable(p.getReturnQty()).orElse(0);
                        if (requestedReturnQty <= 0) {
                                throw new RuntimeException("Invalid return quantity for product: " + product.getId());
                        }

                        // Sold quantity in original sale
                        int soldQty = originalSale.getProducts().stream()
                                        .filter(sp -> sp.getProduct() != null
                                                        && sp.getProduct().getId().equals(product.getId()))
                                        .mapToInt(sp -> sp.getSaleQty())
                                        .sum();

                        // Already returned quantity
                        int alreadyReturnedQty = saleReturnRepository.sumReturnedQtyBySaleAndProduct(
                                        originalSale.getId(), product.getId());

                        int maxAllowedReturn = soldQty - alreadyReturnedQty;
                        if (requestedReturnQty > maxAllowedReturn) {
                                throw new RuntimeException("Return quantity for product " + product.getName()
                                                + " exceeds sold quantity. Max allowed: " + maxAllowedReturn);
                        }

                        // Fetch ProductPrice for this product, warehouse, company
                        ProductPrice productPrice = productPriceRepository
                                        .findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                                        product.getId(),
                                                        warehouse.getId(),
                                                        null, // or channel if relevant
                                                        user.getCompanyId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "ProductPrice not found for product: " + product.getId()));

                        BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice())
                                        .orElse(productPrice.getPrice());
                        BigDecimal returnDiscount = Optional.ofNullable(p.getReturnDiscount()).orElse(BigDecimal.ZERO);
                        BigDecimal returnTax = Optional.ofNullable(p.getReturnTax()).orElse(BigDecimal.ZERO);

                        // Adjust stock
                        productStockService.adjustStock(
                                        user.getCompanyId(),
                                        user.getId(),
                                        product.getId(),
                                        warehouse.getId(),
                                        requestedReturnQty // increase stock for return
                        );

                        // Optionally update ProductPrice cost if you want latest reference cost
                        if (p.getProductUnitPrice() != null) {
                                productPrice.setCost(unitPrice);
                                productPriceRepository.save(productPrice);
                        }

                        SaleReturnProduct srp = SaleReturnProduct.builder()
                                        .saleReturn(saleReturn)
                                        .product(product)
                                        .returnQty(requestedReturnQty)
                                        .returnDiscount(returnDiscount)
                                        .returnTax(returnTax)
                                        .productUnitPrice(unitPrice)
                                        .createdBy(user.getId())
                                        .createdAt(LocalDateTime.now())
                                        .companyId(user.getCompanyId())
                                        .build();

                        returnProducts.add(srp);
                }

                saleReturn.setProducts(returnProducts);

                // Compute refund totals before payments
                calculateRefundTotals(saleReturn);

                SaleReturn savedReturn = saleReturnRepository.save(saleReturn);

                // Handle optional inline payments (refunds)
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.SALE_RETURN)
                                                .referenceId(savedReturn.getId())
                                                .referenceNumber(savedReturn.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(savedReturn.getCurrency() != null
                                                                                ? savedReturn.getCurrency().getCode()
                                                                                : null))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(savedReturn.getExchangeRate()))
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

                List<PaymentResponse> paymentResponses = paymentService
                                .getPaymentsByReference(PaymentSourceType.SALE_RETURN, savedReturn.getId());

                BigDecimal paid = paymentResponses.stream()
                                .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                savedReturn.setRefundAmountTxnCurrency(
                                Optional.ofNullable(savedReturn.getRefundAmountTxnCurrency()).orElse(BigDecimal.ZERO)
                                                .subtract(paid));
                savedReturn.setRefundAmountBaseCurrency(
                                Optional.ofNullable(savedReturn.getRefundAmountBaseCurrency()).orElse(BigDecimal.ZERO)
                                                .subtract(paid.multiply(
                                                                Optional.ofNullable(savedReturn.getExchangeRate())
                                                                                .orElse(BigDecimal.ONE))));

                if (Optional.ofNullable(savedReturn.getRefundAmountTxnCurrency()).orElse(BigDecimal.ZERO)
                                .compareTo(BigDecimal.ZERO) <= 0) {
                        savedReturn.setReturnStatus(SaleStatus.COMPLETED);
                } else {
                        savedReturn.setReturnStatus(
                                        Optional.ofNullable(savedReturn.getReturnStatus()).orElse(SaleStatus.PENDING));
                }

                saleReturnRepository.save(savedReturn);

                List<PaymentResponse> finalPayments = paymentService
                                .getPaymentsByReference(PaymentSourceType.SALE_RETURN, savedReturn.getId());
                return new SaleReturnResponse(savedReturn, finalPayments);
        }

        @Override
        public SaleReturnResponse getSaleReturnById(Long id) {
                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale Return not found"));

                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.SALE_RETURN,
                                saleReturn.getId());
                return new SaleReturnResponse(saleReturn, payments);
        }

        @Override
        @Transactional
        public SaleReturnResponse updateSaleReturn(Long id, UpdateSaleReturnRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale Return not found"));

                Sale originalSale = saleReturn.getOriginalSale();

                // Rollback previously applied stock increases using ProductStockService
                for (SaleReturnProduct srp : saleReturn.getProducts()) {
                        productStockService.adjustStock(
                                        user.getCompanyId(),
                                        user.getId(),
                                        srp.getProduct().getId(),
                                        saleReturn.getWarehouse().getId(),
                                        -srp.getReturnQty() // rollback
                        );
                }

                List<SaleReturnProduct> updatedProducts = new ArrayList<>();

                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        int requestedReturnQty = Optional.ofNullable(p.getReturnQty()).orElse(0);
                        if (requestedReturnQty <= 0) {
                                throw new RuntimeException("Invalid return quantity for product: " + product.getId());
                        }

                        // Sold quantity in original sale
                        int soldQty = originalSale.getProducts().stream()
                                        .filter(sp -> sp.getProduct() != null
                                                        && sp.getProduct().getId().equals(product.getId()))
                                        .mapToInt(sp -> sp.getSaleQty())
                                        .sum();

                        // Already returned quantity (excluding current return)
                        int alreadyReturnedQty = saleReturnRepository.sumReturnedQtyBySaleAndProduct(
                                        originalSale.getId(), product.getId());

                        int maxAllowedReturn = soldQty - alreadyReturnedQty;
                        if (requestedReturnQty > maxAllowedReturn) {
                                throw new RuntimeException("Return quantity for product " + product.getName()
                                                + " exceeds allowed quantity. Max allowed: " + maxAllowedReturn);
                        }

                        // Re-apply stock
                        productStockService.adjustStock(
                                        user.getCompanyId(),
                                        user.getId(),
                                        product.getId(),
                                        saleReturn.getWarehouse().getId(),
                                        requestedReturnQty);

                        // Fetch ProductPrice for this product, warehouse, company
                        ProductPrice productPrice = productPriceRepository
                                        .findByProductIdAndWarehouseIdAndChannelAndCompanyId(
                                                        product.getId(),
                                                        saleReturn.getWarehouse().getId(),
                                                        null, // or channel if relevant
                                                        user.getCompanyId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "ProductPrice not found for product: " + product.getId()));

                        BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice())
                                        .orElse(productPrice.getPrice());
                        BigDecimal returnDiscount = Optional.ofNullable(p.getReturnDiscount()).orElse(BigDecimal.ZERO);
                        BigDecimal returnTax = Optional.ofNullable(p.getReturnTax()).orElse(BigDecimal.ZERO);

                        // Optionally update cost in ProductPrice if new unit price provided
                        if (p.getProductUnitPrice() != null) {
                                productPrice.setCost(unitPrice);
                                productPriceRepository.save(productPrice);
                        }

                        // Check if exists in previous list
                        SaleReturnProduct existing = saleReturn.getProducts().stream()
                                        .filter(srp -> srp.getProduct() != null
                                                        && srp.getProduct().getId().equals(p.getProductId()))
                                        .findFirst()
                                        .orElse(null);

                        if (existing != null) {
                                existing.setReturnQty(requestedReturnQty);
                                existing.setReturnDiscount(returnDiscount);
                                existing.setReturnTax(returnTax);
                                existing.setProductUnitPrice(unitPrice);
                                existing.setUpdatedBy(user.getId());
                                existing.setUpdatedAt(LocalDateTime.now());
                                updatedProducts.add(existing);
                        } else {
                                SaleReturnProduct srp = SaleReturnProduct.builder()
                                                .saleReturn(saleReturn)
                                                .product(product)
                                                .returnQty(requestedReturnQty)
                                                .returnDiscount(returnDiscount)
                                                .returnTax(returnTax)
                                                .productUnitPrice(unitPrice)
                                                .createdBy(user.getId())
                                                .createdAt(LocalDateTime.now())
                                                .companyId(user.getCompanyId())
                                                .build();
                                updatedProducts.add(srp);
                        }
                }

                saleReturn.setProducts(updatedProducts);

                Optional.ofNullable(request.getDate()).ifPresent(saleReturn::setDate);
                Optional.ofNullable(request.getReturnTax()).ifPresent(saleReturn::setReturnTax);
                Optional.ofNullable(request.getReturnDiscount()).ifPresent(saleReturn::setReturnDiscount);
                Optional.ofNullable(request.getShippingCost()).ifPresent(saleReturn::setShippingCost);
                Optional.ofNullable(request.getShipmentStatus()).ifPresent(saleReturn::setShipmentStatus);
                Optional.ofNullable(request.getReturnStatus()).ifPresent(saleReturn::setReturnStatus);
                Optional.ofNullable(request.getNote()).ifPresent(saleReturn::setNote);

                if (request.getCustomerId() != null) {
                        Customer customer = customerRepository.findById(request.getCustomerId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                        saleReturn.setCustomer(customer);
                }

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        saleReturn.setWarehouse(warehouse);
                }

                saleReturn.setUpdatedBy(user.getId());
                saleReturn.setUpdatedAt(LocalDateTime.now());
                saleReturn.setCompanyId(user.getCompanyId());

                // Recompute totals
                calculateRefundTotals(saleReturn);

                SaleReturn saved = saleReturnRepository.save(saleReturn);

                // Handle payments attached in update (append)
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.SALE_RETURN)
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

                // Reload payments and adjust refund due
                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.SALE_RETURN,
                                saved.getId());
                BigDecimal paid = payments.stream()
                                .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                saved.setRefundAmountTxnCurrency(Optional.ofNullable(saved.getRefundAmountTxnCurrency())
                                .orElse(BigDecimal.ZERO).subtract(paid));
                saved.setRefundAmountBaseCurrency(Optional.ofNullable(saved.getRefundAmountBaseCurrency())
                                .orElse(BigDecimal.ZERO).subtract(
                                                paid.multiply(Optional.ofNullable(saved.getExchangeRate())
                                                                .orElse(BigDecimal.ONE))));

                if (Optional.ofNullable(saved.getRefundAmountTxnCurrency()).orElse(BigDecimal.ZERO)
                                .compareTo(BigDecimal.ZERO) <= 0) {
                        saved.setReturnStatus(SaleStatus.COMPLETED);
                }

                saleReturnRepository.save(saved);

                List<PaymentResponse> finalPayments = paymentService
                                .getPaymentsByReference(PaymentSourceType.SALE_RETURN, saved.getId());
                return new SaleReturnResponse(saved, finalPayments);
        }

        @Override
        @Transactional
        public void deleteSaleReturn(Long id) {
                SaleReturn saleReturn = saleReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale Return not found"));

                // rollback stock increases
                rollbackStock(saleReturn.getProducts());
                saleReturnRepository.delete(saleReturn);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleReturnResponse> getMySaleReturns() {
                User user = UserContext.getAuthenticatedUser(userRepository);
                List<SaleReturn> saleReturns = saleReturnRepository.findByCreatedBy(user.getId());

                return saleReturns.stream()
                                .map(sr -> new SaleReturnResponse(sr,
                                                paymentService.getPaymentsByReference(PaymentSourceType.SALE_RETURN,
                                                                sr.getId())))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleReturnResponse> getAllSaleReturns() {
                User user = UserContext.getAuthenticatedUser(userRepository);

                List<SaleReturn> saleReturns = saleReturnRepository.findByCompanyId(user.getCompanyId());

                return saleReturns.stream()
                                .map(sr -> new SaleReturnResponse(sr,
                                                paymentService.getPaymentsByReference(PaymentSourceType.SALE_RETURN,
                                                                sr.getId())))
                                .collect(Collectors.toList());
        }

        // Helpers
        private void rollbackStock(List<SaleReturnProduct> products) {
                if (products == null || products.isEmpty()) {
                        return;
                }

                for (SaleReturnProduct oldProduct : products) {
                        Product product = oldProduct.getProduct();
                        if (product != null) {
                                // Use ProductStockService to adjust stock safely
                                productStockService.adjustStock(
                                                product.getCompanyId(),
                                                oldProduct.getUpdatedBy() != null ? oldProduct.getUpdatedBy()
                                                                : oldProduct.getCreatedBy(),
                                                product.getId(),
                                                oldProduct.getSaleReturn().getWarehouse().getId(),
                                                -oldProduct.getReturnQty() // rollback
                                );
                        }
                }
        }

        private BigDecimal nvl(BigDecimal val) {
                return val == null ? BigDecimal.ZERO : val;
        }

        private void calculateRefundTotals(SaleReturn saleReturn) {
                BigDecimal subtotal = saleReturn.getProducts().stream()
                                .map(p -> {
                                        BigDecimal unit = Optional.ofNullable(p.getProductUnitPrice())
                                                        .orElse(BigDecimal.ZERO);
                                        BigDecimal qty = BigDecimal
                                                        .valueOf(Optional.ofNullable(p.getReturnQty()).orElse(0));
                                        BigDecimal base = unit.multiply(qty);
                                        BigDecimal afterDiscount = base.subtract(nvl(p.getReturnDiscount()));
                                        return afterDiscount.add(nvl(p.getReturnTax()));
                                })
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalTxnCurrency = subtotal
                                .subtract(nvl(saleReturn.getReturnDiscount()))
                                .add(nvl(saleReturn.getReturnTax()))
                                .add(nvl(saleReturn.getShippingCost()));

                saleReturn.setRefundAmountTxnCurrency(totalTxnCurrency);
                saleReturn.setRefundAmountBaseCurrency(totalTxnCurrency
                                .multiply(Optional.ofNullable(saleReturn.getExchangeRate()).orElse(BigDecimal.ONE)));
        }
}
