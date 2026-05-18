package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.PaymentProcessingException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CardPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionLogRepository transactionLogRepository;
    private final ObjectMapper objectMapper;

    public CardPaymentStrategy(PaymentRepository paymentRepository,
            PaymentTransactionLogRepository transactionLogRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionLogRepository = transactionLogRepository;
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
                request.getReferenceId(), request.getAmountTxnCurrency());

        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        try {
            boolean paymentSuccess = simulateCardGatewayCall(request.getPaymentMetadata());
            if (!paymentSuccess) {
                throw new PaymentProcessingException("Card payment failed");
            }

            Map<String, Object> metadata = Collections.singletonMap("cardData", request.getPaymentMetadata());

            BigDecimal baseAmount = request.getAmountBaseCurrency() != null
                    ? request.getAmountBaseCurrency()
                    : request.getAmountTxnCurrency().multiply(request.getExchangeRate());

            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmountTxnCurrency())
                    .amountBaseCurrency(baseAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.CARD)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference("CARD-TXN-" + System.currentTimeMillis())
                    .idempotencyKey(request.getIdempotencyKey())
                    .paymentMetadata(objectMapper.writeValueAsString(metadata))
                    .createdBy(currentUserId)
                    .updatedBy(currentUserId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .companyId(currentCompanyId)
                    .referenceCurrencyCode(request.getReferenceCurrencyCode())
                    .referenceAmount(request.getReferenceAmount())
                    .warehouseId(request.getWarehouseId())
                    .posTerminalId(request.getPosTerminalId())
                    .exchangeRateSource(request.getExchangeRateSource())
                    .build();

            Payment saved = paymentRepository.save(payment);

            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(saved)
                    .companyId(saved.getCompanyId())
                    .executedBy(currentUserId)
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload(request.getPaymentMetadata())
                    .responsePayload("Card payment successful")
                    .status(PaymentStatus.PAID)
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return buildResponse(saved, metadata);

        } catch (Exception e) {
            log.error("Card payment error for referenceId={}, message={}", request.getReferenceId(), e.getMessage(), e);

            Payment failedPayment = saveFailedPayment(request, currentUserId, currentCompanyId, e);
            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(failedPayment)
                    .companyId(currentCompanyId)
                    .executedBy(currentUserId)
                    .gatewayProvider(PaymentGatewayProvider.STRIPE)
                    .requestPayload(request.getPaymentMetadata())
                    .responsePayload("Error: " + e.getMessage())
                    .status(PaymentStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();
            try {
                transactionLogRepository.save(logEntry);
            } catch (Exception logEx) {
                log.error("Failed to log Card transaction error", logEx);
            }

            throw new PaymentProcessingException("Card payment failed: " + e.getMessage());
        }
    }

    private Payment saveFailedPayment(CreatePaymentRequest request, Long userId, Long companyId, Exception e) {
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
                .paymentMethod(PaymentMethod.CARD)
                .status(PaymentStatus.FAILED)
                .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                .idempotencyKey(request.getIdempotencyKey())
                .paymentMetadata(
                        "{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                .createdBy(userId)
                .updatedBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .companyId(companyId)
                .referenceCurrencyCode(request.getReferenceCurrencyCode())
                .referenceAmount(request.getReferenceAmount())
                .warehouseId(request.getWarehouseId())
                .posTerminalId(request.getPosTerminalId())
                .exchangeRateSource(request.getExchangeRateSource())
                .build();
        return paymentRepository.save(failed);
    }

    private boolean simulateCardGatewayCall(String payload) {
        log.debug("Simulating Card gateway call with payload={}", payload);
        return payload != null && !payload.isBlank();
    }

    private PaymentResponse buildResponse(Payment payment, Map<String, Object> metadata) {
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
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .idempotencyKey(payment.getIdempotencyKey())
                .paymentMetadata(metadata)
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
}