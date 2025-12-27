package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.PaymentProcessingException;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.PaymentTransactionLog;
import nextpos.app.nextpos.model.entity.User;
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
public class PaypalPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionLogRepository transactionLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public PaypalPaymentStrategy(PaymentRepository paymentRepository,
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
        return PaymentMethod.PAYPAL.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        log.info("Initiating PayPal payment: referenceType={}, referenceId={}, amount={}",
                request.getReferenceType(), request.getReferenceId(), request.getAmount());

        User user = UserContext.getAuthenticatedUser(userRepository);

        try {
            // Simulate PayPal gateway
            boolean paymentSuccess = simulatePaypalGatewayCall(request.getPaymentData());
            if (!paymentSuccess) {
                throw new PaymentProcessingException("PayPal payment failed");
            }

            Map<String, Object> metadata = Collections.singletonMap("paypalToken", request.getPaymentData());

            BigDecimal baseAmount = request.getBaseCurrencyAmount() != null
                    ? request.getBaseCurrencyAmount().setScale(4, RoundingMode.HALF_UP)
                    : request.getAmount().multiply(request.getExchangeRate()).setScale(4, RoundingMode.HALF_UP);

            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmount().setScale(4, RoundingMode.HALF_UP))
                    .amountBaseCurrency(baseAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.PAYPAL)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference("PAYPAL-TXN-" + System.currentTimeMillis())
                    .idempotencyKey(request.getIdempotencyKey())
                    .paymentMetadata(objectMapper.writeValueAsString(metadata))
                    .createdBy(user.getId())
                    .updatedBy(user.getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .companyId(user.getCompanyId())
                    .build();

            Payment saved = paymentRepository.save(payment);

            // Log transaction
            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(saved)
                    .executedBy(user.getId())
                    .companyId(user.getCompanyId())
                    .requestPayload(request.getPaymentData())
                    .responsePayload("Payment successful via PayPal")
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return buildPaymentResponse(saved, "PayPal Payment Successful");

        } catch (Exception e) {
            log.error("PayPal payment error for referenceId={}, message={}", request.getReferenceId(), e.getMessage(),
                    e);
            try {
                PaymentTransactionLog failedLog = PaymentTransactionLog.builder()
                        .executedBy(user.getId())
                        .companyId(user.getCompanyId())
                        .requestPayload(request.getPaymentData())
                        .responsePayload("Error: " + e.getMessage())
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .build();
                transactionLogRepository.save(failedLog);
            } catch (Exception logEx) {
                log.error("Failed to log PayPal transaction error", logEx);
            }
            throw new PaymentProcessingException("PayPal payment failed: " + e.getMessage(), e);
        }
    }

    private boolean simulatePaypalGatewayCall(String payload) {
        log.debug("Simulating PayPal gateway call with payload={}", payload);
        return payload != null && !payload.isBlank();
    }

    private Map<String, Object> deserializeMetadata(String metadata) {
        if (metadata == null || metadata.isBlank())
            return Collections.emptyMap();
        try {
            return objectMapper.readValue(metadata, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.warn("Failed to parse payment metadata, returning empty map", e);
            return Collections.emptyMap();
        }
    }

    private PaymentResponse buildPaymentResponse(Payment payment, String message) {
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
                .transactionReference(payment.getTransactionReference())
                .status(payment.getStatus())
                .idempotencyKey(payment.getIdempotencyKey())
                .paymentMetadata(deserializeMetadata(payment.getPaymentMetadata()))
                .companyId(payment.getCompanyId())
                .createdBy(payment.getCreatedBy())
                .createdAt(payment.getCreatedAt())
                .updatedBy(payment.getUpdatedBy())
                .updatedAt(payment.getUpdatedAt())
                .message(message)
                .build();
    }
}
