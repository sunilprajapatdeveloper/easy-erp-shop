package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.PaymentProcessingException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CardPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionLogRepository transactionLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public CardPaymentStrategy(PaymentRepository paymentRepository,
            PaymentTransactionLogRepository transactionLogRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(String method) {
        return PaymentMethod.CARD.name().equalsIgnoreCase(method);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        log.info("Processing CARD payment for referenceId={}, amount={}",
                request.getReferenceId(), request.getAmount());

        User user = UserContext.getAuthenticatedUser(userRepository);

        try {
            boolean paymentSuccess = simulateCardGatewayCall(request.getPaymentData());
            if (!paymentSuccess) {
                throw new PaymentProcessingException("Card payment failed");
            }

            Map<String, Object> metadata = Collections.singletonMap("cardData", request.getPaymentData());

            BigDecimal baseAmount = request.getBaseCurrencyAmount() != null
                    ? request.getBaseCurrencyAmount()
                    : request.getAmount().multiply(request.getExchangeRate());

            // Save Payment
            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmount())
                    .amountBaseCurrency(baseAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.CARD)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference("CARD-TXN-" + System.currentTimeMillis())
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
                    .companyId(saved.getCompanyId())
                    .executedBy(user.getId())
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload(request.getPaymentData())
                    .responsePayload("Card payment successful")
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return PaymentResponse.builder()
                    .id(saved.getId())
                    .referenceNumber(saved.getReferenceNumber())
                    .referenceType(saved.getReferenceType())
                    .referenceId(saved.getReferenceId())
                    .paymentType(saved.getPaymentType())
                    .amount(saved.getAmountTxnCurrency())
                    .baseCurrencyAmount(saved.getAmountBaseCurrency())
                    .currencyCode(saved.getCurrencyCode())
                    .exchangeRate(saved.getExchangeRate())
                    .paymentMethod(saved.getPaymentMethod())
                    .status(saved.getStatus())
                    .transactionReference(saved.getTransactionReference())
                    .idempotencyKey(saved.getIdempotencyKey())
                    .paymentMetadata(metadata)
                    .companyId(saved.getCompanyId())
                    .createdBy(saved.getCreatedBy())
                    .createdAt(saved.getCreatedAt())
                    .updatedBy(saved.getUpdatedBy())
                    .updatedAt(saved.getUpdatedAt())
                    .message("Card Payment Successful")
                    .build();

        } catch (Exception e) {
            log.error("Card payment error for referenceId={}, message={}", request.getReferenceId(), e.getMessage(), e);

            try {
                PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                        .companyId(user.getCompanyId())
                        .executedBy(user.getId())
                        .gatewayProvider(PaymentGatewayProvider.STRIPE)
                        .requestPayload(request.getPaymentData())
                        .responsePayload("Error: " + e.getMessage())
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .build();
                transactionLogRepository.save(logEntry);
            } catch (Exception logEx) {
                log.error("Failed to log Card transaction error", logEx);
            }

            throw new PaymentProcessingException("Card payment failed: " + e.getMessage());
        }
    }

    private boolean simulateCardGatewayCall(String payload) {
        log.debug("Simulating Card gateway call with payload={}", payload);
        return payload != null && !payload.isBlank();
    }
}
