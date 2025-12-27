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
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.PaymentTransactionLogRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import org.json.JSONObject;
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
public class UpiPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionLogRepository transactionLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UpiPaymentStrategy(PaymentRepository paymentRepository,
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
        return PaymentMethod.UPI.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        log.info("Initiating UPI payment for referenceId={} amount={}",
                request.getReferenceId(), request.getAmount());

        User user = UserContext.getAuthenticatedUser(userRepository);

        try {
            PaymentGatewayProvider gateway = determineGateway(request);
            JSONObject metadata = generateUpiMetadata(request, gateway);

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
                    .paymentMethod(PaymentMethod.UPI)
                    .gatewayProvider(gateway)
                    .status(PaymentStatus.PENDING)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference(metadata.optString("transactionId"))
                    .idempotencyKey(request.getIdempotencyKey())
                    .paymentMetadata(metadata.toString())
                    .createdBy(user.getId())
                    .updatedBy(user.getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .companyId(user.getCompanyId())
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            // Log transaction
            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(savedPayment)
                    .executedBy(user.getId())
                    .companyId(savedPayment.getCompanyId())
                    .gatewayProvider(gateway)
                    .requestPayload(request.getPaymentData())
                    .responsePayload(metadata.toString())
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return buildPaymentResponse(savedPayment, "UPI Payment initiated successfully via " + gateway.name());

        } catch (Exception ex) {
            log.error("Failed to initiate UPI payment", ex);

            try {
                PaymentTransactionLog failedLog = PaymentTransactionLog.builder()
                        .executedBy(user.getId())
                        .companyId(user.getCompanyId())
                        .gatewayProvider(PaymentGatewayProvider.UNKNOWN)
                        .requestPayload(request.getPaymentData())
                        .responsePayload("Error: " + ex.getMessage())
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .build();
                transactionLogRepository.save(failedLog);
            } catch (Exception logEx) {
                log.error("Failed to log UPI transaction error", logEx);
            }

            throw new PaymentProcessingException("Failed to initiate UPI payment: " + ex.getMessage(), ex);
        }
    }

    private PaymentGatewayProvider determineGateway(CreatePaymentRequest request) {
        // Dynamic selection logic can be implemented here
        return PaymentGatewayProvider.RAZORPAY;
    }

    private JSONObject generateUpiMetadata(CreatePaymentRequest request, PaymentGatewayProvider gateway) {
        JSONObject metadata = new JSONObject();
        metadata.put("gateway", gateway.name());
        metadata.put("amount", request.getAmount());
        metadata.put("currency", request.getCurrencyCode());

        String txnId = gateway.name() + "_TXN_" + System.currentTimeMillis();
        metadata.put("transactionId", txnId);
        metadata.put("upiLink", "upi://pay?pa=" + gateway.name().toLowerCase() + "@upi&pn=NextPOS&tr="
                + txnId + "&am=" + request.getAmount() + "&cu=" + request.getCurrencyCode());

        return metadata;
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
                .gatewayProvider(payment.getGatewayProvider())
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
