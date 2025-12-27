package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.PaymentTransactionLog;
import nextpos.app.nextpos.model.entity.User;
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
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public CashPaymentStrategy(PaymentRepository paymentRepository,
            PaymentTransactionLogRepository transactionLogRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.CASH.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        // log.info("Processing cash payment for referenceType={} referenceId={}
        // amount={}", request.getReferenceType(), request.getReferenceId(),
        // request.getAmount());

        User user = UserContext.getAuthenticatedUser(userRepository);

        try {
            // Compute base currency amount if not provided
            BigDecimal baseAmount = request.getBaseCurrencyAmount() != null
                    ? request.getBaseCurrencyAmount().setScale(4, RoundingMode.HALF_UP)
                    : request.getAmount().multiply(request.getExchangeRate()).setScale(4, RoundingMode.HALF_UP);

            // Build Payment entity
            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmount().setScale(4, RoundingMode.HALF_UP))
                    .amountBaseCurrency(baseAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.CASH)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference(
                            firstNonBlank(request.getTransactionReference(), request.getReferenceNumber()))
                    .idempotencyKey(request.getIdempotencyKey())
                    // Serialize metadata Map to JSON string
                    .paymentMetadata(objectMapper.writeValueAsString(normalizeMetadata(request.getPaymentData())))
                    .createdBy(user.getId())
                    .updatedBy(user.getId())
                    .companyId(user.getCompanyId())
                    .build();

            Payment saved = paymentRepository.save(payment);

            // Log transaction
            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(saved)
                    .executedBy(user.getId())
                    .companyId(saved.getCompanyId())
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload("Cash payment received")
                    .responsePayload("Payment saved successfully")
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .build();

            try {
                transactionLogRepository.save(logEntry);
            } catch (Exception ignore) {
                log.error("Failed to save failed payment log", ignore);
            }

            return toPaymentResponse(saved);

        } catch (Exception ex) {
            // log.error("Error while processing cash payment", ex);

            PaymentTransactionLog failedLog = PaymentTransactionLog.builder()
                    .executedBy(user.getId())
                    .companyId(user.getCompanyId())
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload("Cash payment failed")
                    .responsePayload("Error: " + ex.getMessage())
                    .success(false)
                    .timestamp(LocalDateTime.now())
                    .build();

            try {
                transactionLogRepository.save(failedLog);
            } catch (Exception ignore) {
                log.error("Failed to save failed payment log", ignore);
            }

            throw new RuntimeException("Cash payment failed: " + ex.getMessage(), ex);
        }
    }

    private Map<String, Object> normalizeMetadata(String metadata) {
        if (metadata == null || metadata.isBlank())
            return Collections.emptyMap();
        try {
            return objectMapper.readValue(metadata, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            // log.warn("Failed to parse payment metadata, returning empty map", e);
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
                .amount(payment.getAmountTxnCurrency())
                .baseCurrencyAmount(payment.getAmountBaseCurrency())
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
                .message("Cash Payment Successful")
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
