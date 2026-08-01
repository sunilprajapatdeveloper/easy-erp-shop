package nextpos.app.nextpos.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.PaymentProcessingException;
import nextpos.app.nextpos.factory.PaymentStrategyFactory;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.SaleRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.strategy.PaymentStrategy;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;
    private final PaymentStrategyFactory strategyFactory;
    private final WarehouseAccessService warehouseAccessService;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        if (request.getWarehouseId() != null) {
            warehouseAccessService.requireAccessible(request.getWarehouseId());
        }
        if (request.getReferenceType() == PaymentSourceType.SALE && request.getReferenceId() != null) {
            Sale sale = saleRepository.findByIdAndCompanyId(request.getReferenceId(), companyId)
                    .orElseThrow(() -> new PaymentProcessingException("Referenced sale not found"));
            warehouseAccessService.requireAssignment(sale.getWarehouse().getId());
            if (request.getWarehouseId() != null
                    && !request.getWarehouseId().equals(sale.getWarehouse().getId())) {
                throw new PaymentProcessingException("Payment warehouse does not match referenced sale");
            }
        }

        // Check idempotency
        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            Optional<Payment> existing = paymentRepository.findByIdempotencyKeyAndCompanyId(
                    request.getIdempotencyKey(), companyId);
            if (existing.isPresent()) {
                log.info("Idempotent createPayment hit for key={} companyId={}",
                        request.getIdempotencyKey(), companyId);
                return toResponse(existing.get());
            }
        }

        PaymentResponse response;

        // Resolve payment method from string
        PaymentMethod methodEnum = request.getPaymentMethod();

        PaymentStrategy strategy = strategyFactory.getStrategy(methodEnum);

        try {
            response = strategy.pay(request);
            return response;
        } catch (Exception ex) {
            log.error("Payment failed: {}", ex.getMessage());
            throw new PaymentProcessingException("Payment failed: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(Long saleId, CreatePaymentRequest request) {
        // Fetch the associated Sale to get its reference number
        Sale sale = saleRepository.findByIdAndCompanyId(saleId, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + saleId));
        warehouseAccessService.requireAssignment(sale.getWarehouse().getId());

        // Enrich the payment request with the actual sale reference info
        CreatePaymentRequest enrichedRequest = CreatePaymentRequest.builder()
                .referenceType(PaymentSourceType.SALE)
                .referenceId(sale.getId())
                .referenceNumber(sale.getReferenceNumber())
                .paymentType(request.getPaymentType())
                .amountTxnCurrency(request.getAmountTxnCurrency())
                .currencyCode(request.getCurrencyCode())
                .exchangeRate(request.getExchangeRate())
                .amountBaseCurrency(request.getAmountBaseCurrency())
                .paymentMethod(request.getPaymentMethod())
                .paymentMetadata(request.getPaymentMetadata())
                .paymentDate(request.getPaymentDate())
                .transactionReference(request.getTransactionReference())
                .idempotencyKey(request.getIdempotencyKey())
                .status(request.getStatus())
                .referenceCurrencyCode(sale.getCurrency() != null ? sale.getCurrency().getCode() : null)
                .referenceAmount(sale.getTotalAmountTxnCurrency())
                .warehouseId(sale.getWarehouse() != null ? sale.getWarehouse().getId() : null)
                .posTerminalId(sale.getPosTerminalId())
                .exchangeRateSource(request.getExchangeRateSource())
                .build();

        return createPayment(enrichedRequest);
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, UpdatePaymentRequest request) {
        Long userId = UserContext.getCurrentUserId();
        Long companyId = UserContext.getCurrentCompanyId();

        Payment payment = paymentRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (payment.getWarehouseId() != null) warehouseAccessService.requireAssignment(payment.getWarehouseId());

        boolean recalcBase = false;

        if (request.getAmountTxnCurrency() != null) {
            payment.setAmountTxnCurrency(scale4(request.getAmountTxnCurrency()));
            recalcBase = true;
        }

        if (request.getCurrencyCode() != null && !request.getCurrencyCode().isBlank()) {
            payment.setCurrencyCode(request.getCurrencyCode().trim());
        }

        if (request.getExchangeRate() != null) {
            payment.setExchangeRate(scale8(request.getExchangeRate()));
            recalcBase = true;
        }

        if (request.getAmountBaseCurrency() != null) {
            payment.setAmountBaseCurrency(scale4(request.getAmountBaseCurrency()));
            recalcBase = false; // explicit value overrides recompute
        }

        if (recalcBase) {
            if (payment.getAmountTxnCurrency() != null && payment.getExchangeRate() != null) {
                payment.setAmountBaseCurrency(
                        multiplyToBase(payment.getAmountTxnCurrency(), payment.getExchangeRate()));
            }
        }

        if (request.getPaymentMethod() != null) {
            payment.setPaymentMethod(request.getPaymentMethod());
        }

        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }

        if (request.getPaymentDate() != null) {
            payment.setPaymentDate(request.getPaymentDate());
        }

        if (request.getTransactionReference() != null && !request.getTransactionReference().isBlank()) {
            payment.setTransactionReference(request.getTransactionReference().trim());
        }

        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            payment.setIdempotencyKey(request.getIdempotencyKey().trim());
        }

        if (request.getReferenceCurrencyCode() != null) {
            payment.setReferenceCurrencyCode(request.getReferenceCurrencyCode());
        }
        if (request.getReferenceAmount() != null) {
            payment.setReferenceAmount(scale4(request.getReferenceAmount()));
        }
        if (request.getWarehouseId() != null) {
            warehouseAccessService.requireAccessible(request.getWarehouseId());
            payment.setWarehouseId(request.getWarehouseId());
        }
        if (request.getPosTerminalId() != null) {
            payment.setPosTerminalId(request.getPosTerminalId());
        }
        if (request.getExchangeRateSource() != null) {
            payment.setExchangeRateSource(request.getExchangeRateSource());
        }

        payment.setUpdatedBy(userId);
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (payment.getWarehouseId() != null) warehouseAccessService.requireAssignment(payment.getWarehouseId());
        paymentRepository.delete(payment);
    }

    @Override
    public PaymentResponse getPayment(Long id) {
        Payment payment = paymentRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (payment.getWarehouseId() != null) warehouseAccessService.requireAssignment(payment.getWarehouseId());
        return toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByReference(PaymentSourceType referenceType, Long referenceId) {
        return paymentRepository.findByReferenceTypeAndReferenceIdAndCompanyId(
                        referenceType, referenceId, UserContext.getCurrentCompanyId())
                .stream()
                .filter(payment -> payment.getWarehouseId() == null
                        || UserContext.canAccessWarehouse(payment.getWarehouseId())
                        || UserContext.getAuthenticatedUser().getAuthorities().stream()
                                .anyMatch(a -> "ROLE_COMPANY_OWNER".equals(a.getAuthority())))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse.PaymentResponseBuilder builder = PaymentResponse.builder()
                .id(payment.getId())
                .referenceNumber(payment.getReferenceNumber())
                .referenceType(payment.getReferenceType())
                .referenceId(payment.getReferenceId())
                .paymentType(payment.getPaymentType())
                .amountTxnCurrency(payment.getAmountTxnCurrency())
                .currencyCode(payment.getCurrencyCode())
                .exchangeRate(payment.getExchangeRate())
                .amountBaseCurrency(payment.getAmountBaseCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .gatewayProvider(payment.getGatewayProvider())
                .paymentDate(payment.getPaymentDate())
                .transactionReference(payment.getTransactionReference())
                .idempotencyKey(payment.getIdempotencyKey())
                .status(payment.getStatus())
                .companyId(payment.getCompanyId())
                .createdBy(payment.getCreatedBy())
                .createdAt(payment.getCreatedAt())
                .updatedBy(payment.getUpdatedBy())
                .updatedAt(payment.getUpdatedAt())
                .referenceCurrencyCode(payment.getReferenceCurrencyCode())
                .referenceAmount(payment.getReferenceAmount())
                .warehouseId(payment.getWarehouseId())
                .posTerminalId(payment.getPosTerminalId())
                .exchangeRateSource(payment.getExchangeRateSource());

        if (payment.getPaymentMetadata() != null) {
            try {
                JSONObject meta = new JSONObject(payment.getPaymentMetadata());
                builder.paymentMetadata(meta.toMap());
            } catch (Exception e) {
                log.debug("Non-JSON paymentMetadata stored for payment {}", payment.getId());
            }
        }

        return builder.build();
    }

    private BigDecimal scale4(BigDecimal v) {
        return v == null ? null : v.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal scale8(BigDecimal v) {
        return v == null ? null : v.setScale(8, RoundingMode.HALF_UP);
    }

    private BigDecimal multiplyToBase(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(4, RoundingMode.HALF_UP);
    }
}
