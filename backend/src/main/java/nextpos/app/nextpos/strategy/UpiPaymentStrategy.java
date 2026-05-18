package nextpos.app.nextpos.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
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
    private final ObjectMapper objectMapper;

    public UpiPaymentStrategy(PaymentRepository paymentRepository,
            PaymentTransactionLogRepository transactionLogRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.UPI.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        try {
            PaymentGatewayProvider gateway = determineGateway(request);
            JSONObject metadata = generateUpiMetadata(request, gateway);

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
                    .paymentMethod(PaymentMethod.UPI)
                    .gatewayProvider(gateway)
                    .status(PaymentStatus.PENDING)
                    .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
                    .transactionReference(metadata.optString("transactionId"))
                    .idempotencyKey(request.getIdempotencyKey())
                    .paymentMetadata(metadata.toString())
                    .createdBy(currentUserId)
                    .updatedBy(currentUserId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .companyId(currentCompanyId)
                    .referenceCurrencyCode(request.getReferenceCurrencyCode())
                    .referenceAmount(request.getReferenceAmount() != null
                            ? request.getReferenceAmount().setScale(4, RoundingMode.HALF_UP)
                            : null)
                    .warehouseId(request.getWarehouseId())
                    .posTerminalId(request.getPosTerminalId())
                    .exchangeRateSource(request.getExchangeRateSource())
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            PaymentTransactionLog logEntry = PaymentTransactionLog.builder()
                    .payment(savedPayment)
                    .executedBy(currentUserId)
                    .companyId(savedPayment.getCompanyId())
                    .gatewayProvider(gateway)
                    .requestPayload(request.getPaymentMetadata())
                    .responsePayload(metadata.toString())
                    .status(PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionLogRepository.save(logEntry);

            return buildResponse(savedPayment);

        } catch (Exception ex) {
            log.error("Failed to initiate UPI payment", ex);
            Payment failedPayment = saveFailedPayment(request, currentUserId, currentCompanyId, ex);
            PaymentTransactionLog failedLog = PaymentTransactionLog.builder()
                    .payment(failedPayment)
                    .executedBy(currentUserId)
                    .companyId(currentCompanyId)
                    .gatewayProvider(PaymentGatewayProvider.UNKNOWN)
                    .requestPayload(request.getPaymentMetadata())
                    .responsePayload("Error: " + ex.getMessage())
                    .status(PaymentStatus.FAILED)
                    .errorMessage(ex.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();
            try {
                transactionLogRepository.save(failedLog);
            } catch (Exception logEx) {
                log.error("Failed to log UPI transaction error", logEx);
            }
            throw new PaymentProcessingException("Failed to initiate UPI payment: " + ex.getMessage(), ex);
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
                .paymentMethod(PaymentMethod.UPI)
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

    private PaymentGatewayProvider determineGateway(CreatePaymentRequest request) {
        return PaymentGatewayProvider.RAZORPAY;
    }

    private JSONObject generateUpiMetadata(CreatePaymentRequest request, PaymentGatewayProvider gateway) {
        JSONObject metadata = new JSONObject();
        metadata.put("gateway", gateway.name());
        metadata.put("amount", request.getAmountTxnCurrency());
        metadata.put("currency", request.getCurrencyCode());

        String txnId = gateway.name() + "_TXN_" + System.currentTimeMillis();
        metadata.put("transactionId", txnId);
        metadata.put("upiLink", "upi://pay?pa=" + gateway.name().toLowerCase() + "@upi&pn=NextPOS&tr="
                + txnId + "&am=" + request.getAmountTxnCurrency() + "&cu=" + request.getCurrencyCode());

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

    private PaymentResponse buildResponse(Payment payment) {
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
                .status(payment.getStatus())
                .idempotencyKey(payment.getIdempotencyKey())
                .paymentMetadata(deserializeMetadata(payment.getPaymentMetadata()))
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
