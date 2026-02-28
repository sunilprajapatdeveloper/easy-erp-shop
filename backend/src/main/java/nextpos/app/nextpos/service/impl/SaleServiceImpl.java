package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleProduct;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.SaleRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.SaleService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

        private final SaleRepository saleRepository;
        private final UserRepository userRepository;
        private final CustomerRepository customerRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CurrencyRepository currencyRepository;
        private final PaymentService paymentService;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public SaleResponse createSale(CreateSaleRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                Customer customer = null;
                if (request.getCustomerId() != null) {
                        customer = customerRepository.findById(request.getCustomerId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                }

                // load currency from request
                Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new RuntimeException("Currency not found"));

                BigDecimal exchangeRate = request.getExchangeRate();
                if (exchangeRate == null) {
                        throw new RuntimeException("Exchange rate is required");
                }

                Sale sale = Sale.builder()
                                .referenceNumber(ReferenceNumberGenerator.generateReferenceNumber("SALE"))
                                .date(request.getDate())
                                .warehouse(warehouse)
                                .customer(customer)
                                .currency(currency)
                                .exchangeRate(exchangeRate)
                                .companyId(user.getCompanyId())
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .shipmentStatus(Optional.ofNullable(request.getShipmentStatus())
                                                .orElse(ShipmentStatus.PENDING))
                                .saleStatus(Optional.ofNullable(request.getSaleStatus()).orElse(SaleStatus.PENDING))
                                .source(Optional.ofNullable(request.getSource()).orElse(SaleSource.WEB))
                                .note(request.getNote())
                                .orderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO))
                                .discount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO))
                                .shippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO))
                                .build();

                // Handle sale products
                BigDecimal totalTxn = BigDecimal.ZERO;
                List<SaleProduct> saleProducts = new ArrayList<>();

                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = Optional.ofNullable(p.getSaleQty()).orElse(0);

                        // Stock validation
                        ProductStockResponse stock = productStockService.getByProductAndWarehouse(
                                        product.getId(),
                                        warehouse.getId());

                        if (stock.getQuantity() < qty) {
                                throw new RuntimeException("Insufficient stock for product: " + product.getName());
                        }

                        // Deduct stock atomically
                        productStockService.adjustStock(product.getId(),
                                        warehouse.getId(), -qty);

                        BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice()).orElse(BigDecimal.ZERO);
                        BigDecimal discount = Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO);
                        BigDecimal tax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty))
                                        .subtract(discount)
                                        .add(tax);

                        totalTxn = totalTxn.add(lineTotal);

                        saleProducts.add(SaleProduct.builder()
                                        .sale(sale)
                                        .product(product)
                                        .productUnitPrice(unitPrice)
                                        .saleQty(qty)
                                        .productDiscount(discount)
                                        .productTax(tax)
                                        .createdBy(user.getId())
                                        .createdAt(LocalDateTime.now())
                                        .companyId(user.getCompanyId())
                                        .build());
                }

                sale.setProducts(saleProducts);

                totalTxn = totalTxn.add(sale.getOrderTax()).subtract(sale.getDiscount()).add(sale.getShippingCost());
                sale.setTotalAmountTxnCurrency(totalTxn);
                sale.setDueAmountTxnCurrency(totalTxn);

                BigDecimal totalBase = totalTxn.multiply(exchangeRate);
                sale.setTotalAmountBaseCurrency(totalBase);
                sale.setDueAmountBaseCurrency(totalBase);

                Sale savedSale = saleRepository.save(sale);

                // Only create payments if request contains them
                List<PaymentResponse> paymentResponses = new ArrayList<>();
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.SALE)
                                                .referenceId(savedSale.getId())
                                                .referenceNumber(savedSale.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(currency.getCode()))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(exchangeRate))
                                                .baseCurrencyAmount(paymentReq.getBaseCurrencyAmount())
                                                .paymentMethod(paymentReq.getPaymentMethod())
                                                .paymentData(paymentReq.getPaymentData())
                                                .status(paymentReq.getStatus())
                                                .paymentDate(paymentReq.getPaymentDate())
                                                .note(paymentReq.getNote())
                                                .transactionReference(paymentReq.getTransactionReference())
                                                .idempotencyKey(paymentReq.getIdempotencyKey())
                                                .build();

                                PaymentResponse response = paymentService.createPayment(enriched);
                                paymentResponses.add(response);
                        }

                        // Update due amounts after payment
                        BigDecimal paid = paymentResponses.stream()
                                        .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        savedSale.setDueAmountTxnCurrency(savedSale.getTotalAmountTxnCurrency().subtract(paid));
                        savedSale.setDueAmountBaseCurrency(savedSale.getTotalAmountBaseCurrency()
                                        .subtract(paid.multiply(exchangeRate)));
                        saleRepository.save(savedSale);
                }

                return new SaleResponse(savedSale, paymentResponses);
        }

        @Override
        @Transactional(readOnly = true)
        public SaleResponse getSaleById(Long id) {
                Sale sale = saleRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale not found"));

                List<PaymentResponse> payments = paymentService
                                .getPaymentsByReference(PaymentSourceType.SALE, sale.getId());

                return new SaleResponse(sale, payments);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleResponse> getMySales() {
                User user = UserContext.getAuthenticatedUser(userRepository);
                List<Sale> sales = saleRepository.findAllByCreatedBy(user.getId());

                return sales.stream()
                                .map(sale -> new SaleResponse(sale,
                                                paymentService.getPaymentsByReference(PaymentSourceType.SALE,
                                                                sale.getId())))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<SaleResponse> getAllSales() {
                User user = UserContext.getAuthenticatedUser(userRepository);
                List<Sale> sales = saleRepository.findAllByCompanyId(user.getCompanyId());

                return sales.stream()
                                .map(sale -> new SaleResponse(sale,
                                                paymentService.getPaymentsByReference(PaymentSourceType.SALE,
                                                                sale.getId())))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public SaleResponse updateSale(Long id, UpdateSaleRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Sale sale = saleRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale not found"));

                // Update warehouse first if provided
                if (request.getWarehouseId() != null) {
                        Warehouse newWarehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        sale.setWarehouse(newWarehouse);
                }

                // Use effectively final warehouse for stock adjustments
                final Warehouse warehouseToUse = sale.getWarehouse();

                // Rollback old stock using ProductStockService
                sale.getProducts().forEach(sp -> {
                        productStockService.adjustStock(
                                        sp.getProduct().getId(),
                                        warehouseToUse.getId(),
                                        sp.getSaleQty() // rollback
                        );
                });

                // Track product IDs from request
                Set<Long> requestProductIds = request.getProducts().stream()
                                .map(UpdateSaleRequest.SaleProductUpdateRequest::getProductId)
                                .collect(Collectors.toSet());

                List<SaleProduct> updatedProducts = new ArrayList<>();

                // Update or add products
                for (var p : request.getProducts()) {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: " + p.getProductId()));

                        int qty = Optional.ofNullable(p.getSaleQty()).orElse(0);

                        // Validate and adjust stock
                        ProductStockResponse stock = productStockService.getByProductAndWarehouse(
                                        product.getId(),
                                        warehouseToUse.getId());

                        if (stock.getQuantity() < qty) {
                                throw new RuntimeException("Insufficient stock for product: " + product.getName());
                        }

                        productStockService.adjustStock(
                                        product.getId(),
                                        warehouseToUse.getId(),
                                        -qty // reduce stock
                        );

                        // Check if product already exists in sale
                        SaleProduct existing = sale.getProducts().stream()
                                        .filter(sp -> sp.getProduct().getId().equals(product.getId()))
                                        .findFirst()
                                        .orElse(null);

                        if (existing != null) {
                                // Update existing product
                                existing.setSaleQty(qty);
                                existing.setProductUnitPrice(
                                                Optional.ofNullable(p.getProductUnitPrice()).orElse(BigDecimal.ZERO));
                                existing.setProductDiscount(
                                                Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO));
                                existing.setProductTax(Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO));
                                existing.setUpdatedBy(user.getId());
                                existing.setUpdatedAt(LocalDateTime.now());
                                updatedProducts.add(existing);
                        } else {
                                // Add new product
                                SaleProduct newProduct = SaleProduct.builder()
                                                .sale(sale)
                                                .product(product)
                                                .productUnitPrice(Optional.ofNullable(p.getProductUnitPrice())
                                                                .orElse(BigDecimal.ZERO))
                                                .saleQty(qty)
                                                .productDiscount(Optional.ofNullable(p.getProductDiscount())
                                                                .orElse(BigDecimal.ZERO))
                                                .productTax(Optional.ofNullable(p.getProductTax())
                                                                .orElse(BigDecimal.ZERO))
                                                .createdBy(user.getId())
                                                .createdAt(LocalDateTime.now())
                                                .companyId(user.getCompanyId())
                                                .build();
                                updatedProducts.add(newProduct);
                        }
                }

                // Remove products no longer in request
                sale.getProducts().removeIf(sp -> !requestProductIds.contains(sp.getProduct().getId()));

                // Set updated products
                sale.setProducts(updatedProducts);

                // Update sale-level fields
                Optional.ofNullable(request.getDate()).ifPresent(sale::setDate);
                Optional.ofNullable(request.getOrderTax()).ifPresent(sale::setOrderTax);
                Optional.ofNullable(request.getDiscount()).ifPresent(sale::setDiscount);
                Optional.ofNullable(request.getShippingCost()).ifPresent(sale::setShippingCost);
                Optional.ofNullable(request.getShipmentStatus()).ifPresent(sale::setShipmentStatus);
                Optional.ofNullable(request.getSaleStatus()).ifPresent(sale::setSaleStatus);
                Optional.ofNullable(request.getSource()).ifPresent(sale::setSource);
                Optional.ofNullable(request.getNote()).ifPresent(sale::setNote);

                if (request.getCustomerId() != null) {
                        Customer customer = customerRepository.findById(request.getCustomerId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                        sale.setCustomer(customer);
                }

                sale.setUpdatedBy(user.getId());
                sale.setUpdatedAt(LocalDateTime.now());

                // Recalculate totals
                BigDecimal totalTxn = sale.getProducts().stream()
                                .map(sp -> sp.getProductUnitPrice()
                                                .multiply(BigDecimal.valueOf(sp.getSaleQty()))
                                                .subtract(Optional.ofNullable(sp.getProductDiscount())
                                                                .orElse(BigDecimal.ZERO))
                                                .add(Optional.ofNullable(sp.getProductTax()).orElse(BigDecimal.ZERO)))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                totalTxn = totalTxn.add(Optional.ofNullable(sale.getOrderTax()).orElse(BigDecimal.ZERO))
                                .subtract(Optional.ofNullable(sale.getDiscount()).orElse(BigDecimal.ZERO))
                                .add(Optional.ofNullable(sale.getShippingCost()).orElse(BigDecimal.ZERO));

                sale.setTotalAmountTxnCurrency(totalTxn);
                sale.setTotalAmountBaseCurrency(
                                totalTxn.multiply(Optional.ofNullable(sale.getExchangeRate()).orElse(BigDecimal.ONE)));

                Sale savedSale = saleRepository.save(sale);

                // Handle payments
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.SALE)
                                                .referenceId(savedSale.getId())
                                                .referenceNumber(savedSale.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(savedSale.getCurrency().getCode()))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(savedSale.getExchangeRate()))
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

                // Recalculate due amounts
                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.SALE,
                                savedSale.getId());
                BigDecimal paid = payments.stream()
                                .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                savedSale.setDueAmountTxnCurrency(savedSale.getTotalAmountTxnCurrency().subtract(paid));
                savedSale.setDueAmountBaseCurrency(savedSale.getTotalAmountBaseCurrency()
                                .subtract(paid.multiply(Optional.ofNullable(savedSale.getExchangeRate())
                                                .orElse(BigDecimal.ONE))));

                saleRepository.save(savedSale);

                return new SaleResponse(savedSale, payments);
        }

        @Override
        @Transactional
        public void deleteSale(Long id) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Sale sale = saleRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale not found"));

                // Use the sale's warehouse for stock adjustments
                final Warehouse warehouse = sale.getWarehouse();

                // Rollback stock for all sale products
                sale.getProducts().forEach(sp -> {
                        productStockService.adjustStock(
                                        sp.getProduct().getId(),
                                        warehouse.getId(),
                                        sp.getSaleQty() // add back sold quantity
                        );
                });

                // Delete the sale
                saleRepository.delete(sale);
        }

        // @Override
        // public BigDecimal getTotalSales() {
        // return saleRepository.sumAllSales();
        // }
}
