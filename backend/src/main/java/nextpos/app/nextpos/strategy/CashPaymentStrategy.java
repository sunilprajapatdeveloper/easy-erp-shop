package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.PaymentTransactionLog;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.PaymentTransactionLogRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CashPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionLogRepository transactionLogRepository;
    private final ObjectMapper objectMapper;

    public CashPaymentStrategy(PaymentRepository paymentRepository,
            PaymentTransactionLogRepository transactionLogRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.CASH.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        try {
            BigDecimal baseAmount = request.getAmountBaseCurrency() != null
                    ? request.getAmountBaseCurrency().setScale(4, RoundingMode.HALF_UP)
                    : request.getAmountTxnCurrency().multiply(request.getExchangeRate()).setScale(4,
                            RoundingMode.HALF_UP);

            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmountTxnCurrency().setScale(4, RoundingMode.HALF_UP))
                    .amountBaseCurrency(baseAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.CASH)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference(
                            firstNonBlank(request.getTransactionReference(), request.getReferenceNumber()))
                    .idempotencyKey(request.getIdempotencyKey())
                    .paymentMetadata(objectMapper.writeValueAsString(normalizeMetadata(request.getPaymentMetadata())))
                    .createdBy(currentUserId)
                    .updatedBy(currentUserId)
                    .companyId(currentCompanyId)
                    .referenceCurrencyCode(request.getReferenceCurrencyCode())
                    .referenceAmount(request.getReferenceAmount() != null
                            ? request.getReferenceAmount().setScale(4, RoundingMode.HALF_UP)
                            : null)
                    .warehouseId(request.getWarehouseId())
                    .posTerminalId(request.getPosTerminalId())
                    .exchangeRateSource(request.getExchangeRateSource())
                    .build();

            Payment saved = paymentRepository.save(payment);

            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(saved)
                    .executedBy(currentUserId)
                    .companyId(saved.getCompanyId())
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload("Cash payment received")
                    .responsePayload("Payment saved successfully")
                    .status(PaymentStatus.PAID)
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return toPaymentResponse(saved);

        } catch (Exception ex) {
            Payment failedPayment = saveFailedPayment(request, currentUserId, currentCompanyId, ex);
            PaymentTransactionLog failedLog = PaymentTransactionLog.builder()
                    .payment(failedPayment)
                    .executedBy(currentUserId)
                    .companyId(currentCompanyId)
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload("Cash payment failed")
                    .responsePayload("Error: " + ex.getMessage())
                    .status(PaymentStatus.FAILED)
                    .errorMessage(ex.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();
            try {
                transactionLogRepository.save(failedLog);
            } catch (Exception ignore) {
                log.error("Failed to save failed payment log", ignore);
            }
            throw new RuntimeException("Cash payment failed: " + ex.getMessage(), ex);
        }
    }

    private Payment saveFailedPayment(CreatePaymentRequest request, Long userId, Long companyId, Exception ex) {
        Payment failed = Payment.builder()
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .referenceNumber(request.getReferenceNumber())
                .paymentType(request.getPaymentType())
                .amountTxnCurrency(request.getAmountTxnCurrency())
                .currencyCode(request.getCurrencyCode())
                .exchangeRate(request.getExchangeRate())
                .amountBaseCurrency(request.getAmountBaseCurrency() != null
                        ? request.getAmountBaseCurrency()
                        : request.getAmountTxnCurrency().multiply(request.getExchangeRate()))
                .paymentMethod(PaymentMethod.CASH)
                .status(PaymentStatus.FAILED)
                .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                .idempotencyKey(request.getIdempotencyKey())
                .paymentMetadata("{\"error\":\"" + ex.getMessage().replace("\"", "\\\"") + "\"}")
                .createdBy(userId)
                .updatedBy(userId)
                .companyId(companyId)
                .referenceCurrencyCode(request.getReferenceCurrencyCode())
                .referenceAmount(request.getReferenceAmount())
                .warehouseId(request.getWarehouseId())
                .posTerminalId(request.getPosTerminalId())
                .exchangeRateSource(request.getExchangeRateSource())
                .build();
        return paymentRepository.save(failed);
    }

    private Map<String, Object> normalizeMetadata(String metadata) {
        if (metadata == null || metadata.isBlank())
            return Collections.emptyMap();
        try {
            return objectMapper.readValue(metadata, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .referenceNumber(payment.getReferenceNumber())
                .referenceType(payment.getReferenceType())
                .referenceId(payment.getReferenceId())
                .paymentType(payment.getPaymentType())
                .amountTxnCurrency(payment.getAmountTxnCurrency())
                .amountBaseCurrency(payment.getAmountBaseCurrency())
                .currencyCode(payment.getCurrencyCode())
                .exchangeRate(payment.getExchangeRate())
                .paymentMethod(payment.getPaymentMethod())
                .gatewayProvider(payment.getGatewayProvider())
                .transactionReference(payment.getTransactionReference())
                .idempotencyKey(payment.getIdempotencyKey())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .paymentMetadata(normalizeMetadata(payment.getPaymentMetadata()))
                .companyId(payment.getCompanyId())
                .createdBy(payment.getCreatedBy())
                .createdAt(payment.getCreatedAt())
                .updatedBy(payment.getUpdatedBy())
                .updatedAt(payment.getUpdatedAt())
                .referenceCurrencyCode(payment.getReferenceCurrencyCode())
                .referenceAmount(payment.getReferenceAmount())
                .warehouseId(payment.getWarehouseId())
                .posTerminalId(payment.getPosTerminalId())
                .exchangeRateSource(payment.getExchangeRateSource())
                .build();
    }

    private String firstNonBlank(String primary, String fallback) {
        if (primary != null && !primary.isBlank())
            return primary;
        if (fallback != null && !fallback.isBlank())
            return fallback;
        return null;
    }
}
