package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import nextpos.app.nextpos.service.interf.PosService;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.util.ReferenceNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PosServiceImpl implements PosService {

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

                Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new RuntimeException("Currency not found"));

                Sale sale = new Sale();
                sale.setReferenceNumber(ReferenceNumberGenerator.generateReferenceNumber("POS-SALE"));
                sale.setDate(request.getDate());
                sale.setWarehouse(warehouse);
                sale.setCustomer(customer);
                sale.setCurrency(currency);
                sale.setCompanyId(user.getCompanyId());
                sale.setCreatedBy(user.getId());
                sale.setCreatedAt(LocalDateTime.now());
                sale.setShipmentStatus(Optional.ofNullable(request.getShipmentStatus()).orElse(ShipmentStatus.PENDING));
                sale.setSaleStatus(Optional.ofNullable(request.getSaleStatus()).orElse(SaleStatus.PENDING));
                sale.setSource(Optional.ofNullable(request.getSource()).orElse(SaleSource.POS));
                sale.setNote(request.getNote());
                sale.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                sale.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                sale.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));
                sale.setExchangeRate(Optional.ofNullable(request.getExchangeRate()).orElse(BigDecimal.ONE));

                List<SaleProduct> saleProducts = new ArrayList<>();
                BigDecimal totalTxn = BigDecimal.ZERO;

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

                totalTxn = totalTxn.add(sale.getOrderTax())
                                .subtract(sale.getDiscount())
                                .add(sale.getShippingCost());

                sale.setTotalAmountTxnCurrency(totalTxn);
                sale.setDueAmountTxnCurrency(totalTxn);
                sale.setTotalAmountBaseCurrency(totalTxn.multiply(sale.getExchangeRate()));
                sale.setDueAmountBaseCurrency(sale.getTotalAmountBaseCurrency());

                Sale savedSale = saleRepository.save(sale);

                List<PaymentResponse> paymentResponses = new ArrayList<>();

                // Create payments if request contains them
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

                                PaymentResponse paymentResponse = paymentService.createPayment(enriched);
                                paymentResponses.add(paymentResponse);
                        }

                        // Recalculate due amounts based on payments
                        BigDecimal paid = paymentResponses.stream()
                                        .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        savedSale.setDueAmountTxnCurrency(savedSale.getTotalAmountTxnCurrency().subtract(paid));
                        savedSale.setDueAmountBaseCurrency(savedSale.getTotalAmountBaseCurrency()
                                        .subtract(paid.multiply(savedSale.getExchangeRate())));

                        saleRepository.save(savedSale);
                }

                return new SaleResponse(savedSale, paymentResponses);
        }

        @Override
        @Transactional
        public SaleResponse updateSale(Long id, UpdateSaleRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Sale sale = saleRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sale not found"));

                // Update basic fields
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
                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        sale.setWarehouse(warehouse);
                }

                // Update products if provided
                if (request.getProducts() != null && !request.getProducts().isEmpty()) {
                        Warehouse warehouse = sale.getWarehouse();

                        // Restore old product quantities using ProductStockService
                        for (SaleProduct oldProduct : sale.getProducts()) {
                                productStockService.adjustStock(
                                                oldProduct.getProduct().getId(),
                                                warehouse.getId(),
                                                oldProduct.getSaleQty() // add back old quantity
                                );
                        }
                        sale.getProducts().clear();

                        List<SaleProduct> updatedProducts = new ArrayList<>();
                        BigDecimal totalTxn = BigDecimal.ZERO;

                        for (var p : request.getProducts()) {
                                Product product = productRepository.findById(p.getProductId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found: " + p.getProductId()));

                                int qty = Optional.ofNullable(p.getSaleQty()).orElse(0);

                                // Validate stock using ProductStockService
                                ProductStockResponse stock = productStockService.getByProductAndWarehouse(
                                                product.getId(),
                                                warehouse.getId());

                                if (stock.getQuantity() < qty) {
                                        throw new RuntimeException(
                                                        "Insufficient stock for product: " + product.getName());
                                }

                                // Deduct new quantity atomically
                                productStockService.adjustStock(product.getId(),
                                                warehouse.getId(), -qty);

                                BigDecimal unitPrice = Optional.ofNullable(p.getProductUnitPrice())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal discount = Optional.ofNullable(p.getProductDiscount())
                                                .orElse(BigDecimal.ZERO);
                                BigDecimal tax = Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO);

                                BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty))
                                                .subtract(discount)
                                                .add(tax);
                                totalTxn = totalTxn.add(lineTotal);

                                updatedProducts.add(SaleProduct.builder()
                                                .sale(sale)
                                                .product(product)
                                                .productUnitPrice(unitPrice)
                                                .saleQty(qty)
                                                .productDiscount(discount)
                                                .productTax(tax)
                                                .updatedBy(user.getId())
                                                .updatedAt(LocalDateTime.now())
                                                .companyId(user.getCompanyId())
                                                .build());
                        }

                        sale.setProducts(updatedProducts);

                        totalTxn = totalTxn.add(Optional.ofNullable(sale.getOrderTax()).orElse(BigDecimal.ZERO))
                                        .subtract(Optional.ofNullable(sale.getDiscount()).orElse(BigDecimal.ZERO))
                                        .add(Optional.ofNullable(sale.getShippingCost()).orElse(BigDecimal.ZERO));

                        sale.setTotalAmountTxnCurrency(totalTxn);
                        sale.setTotalAmountBaseCurrency(totalTxn
                                        .multiply(Optional.ofNullable(sale.getExchangeRate()).orElse(BigDecimal.ONE)));
                        sale.setDueAmountTxnCurrency(totalTxn);
                        sale.setDueAmountBaseCurrency(sale.getTotalAmountBaseCurrency());
                }

                sale.setUpdatedBy(user.getId());
                sale.setUpdatedAt(LocalDateTime.now());

                Sale updatedSale = saleRepository.save(sale);

                // Only create payments if request contains them
                if (request.getPayments() != null && !request.getPayments().isEmpty()) {
                        for (CreatePaymentRequest paymentReq : request.getPayments()) {
                                CreatePaymentRequest enriched = CreatePaymentRequest.builder()
                                                .referenceType(PaymentSourceType.SALE)
                                                .referenceId(updatedSale.getId())
                                                .referenceNumber(updatedSale.getReferenceNumber())
                                                .paymentType(paymentReq.getPaymentType())
                                                .amount(paymentReq.getAmount())
                                                .currencyCode(Optional.ofNullable(paymentReq.getCurrencyCode())
                                                                .orElse(updatedSale.getCurrency().getCode()))
                                                .exchangeRate(Optional.ofNullable(paymentReq.getExchangeRate())
                                                                .orElse(updatedSale.getExchangeRate()))
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

                // Fetch payments for display
                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.SALE,
                                updatedSale.getId());

                // Recalculate due amounts based on payments
                BigDecimal paid = payments.stream()
                                .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                updatedSale.setDueAmountTxnCurrency(updatedSale.getTotalAmountTxnCurrency().subtract(paid));
                updatedSale.setDueAmountBaseCurrency(updatedSale.getTotalAmountBaseCurrency()
                                .subtract(paid.multiply(Optional.ofNullable(updatedSale.getExchangeRate())
                                                .orElse(BigDecimal.ONE))));

                saleRepository.save(updatedSale);

                return new SaleResponse(updatedSale, payments);
        }

        @Override
        public SaleResponse getSaleDetails(Long saleId) {
                Sale sale = saleRepository.findById(saleId)
                                .orElseThrow(() -> new RuntimeException("Sale not found"));

                List<PaymentResponse> payments = paymentService.getPaymentsByReference(PaymentSourceType.SALE,
                                sale.getId());

                return new SaleResponse(sale, payments);
        }

        @Override
        public byte[] generateReceipt(Long saleId) {
                String content = "Receipt for sale ID: " + saleId;
                return content.getBytes();
        }
}
