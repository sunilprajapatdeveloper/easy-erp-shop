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
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.SaleRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.strategy.PaymentStrategy;
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
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);

        // Check idempotency
        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            Optional<Payment> existing = paymentRepository.findByIdempotencyKeyAndCompanyId(
                    request.getIdempotencyKey(), user.getCompanyId());
            if (existing.isPresent()) {
                log.info("Idempotent createPayment hit for key={} companyId={}",
                        request.getIdempotencyKey(), user.getCompanyId());
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
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + saleId));

        // Enrich the payment request with the actual sale reference info
        CreatePaymentRequest enrichedRequest = CreatePaymentRequest.builder()
                .referenceType(PaymentSourceType.SALE)
                .referenceId(sale.getId())
                .referenceNumber(sale.getReferenceNumber())
                .paymentType(request.getPaymentType())
                .amount(request.getAmount())
                .currencyCode(request.getCurrencyCode())
                .exchangeRate(request.getExchangeRate())
                .baseCurrencyAmount(request.getBaseCurrencyAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentData(request.getPaymentData())
                .paymentDate(request.getPaymentDate())
                .note(request.getNote())
                .transactionReference(request.getTransactionReference())
                .idempotencyKey(request.getIdempotencyKey())
                .status(request.getStatus())
                .build();

        return createPayment(enrichedRequest);
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, UpdatePaymentRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        boolean recalcBase = false;

        if (request.getAmount() != null) {
            payment.setAmountTxnCurrency(scale4(request.getAmount()));
            recalcBase = true;
        }

        if (request.getCurrencyCode() != null && !request.getCurrencyCode().isBlank()) {
            payment.setCurrencyCode(request.getCurrencyCode().trim());
        }

        if (request.getExchangeRate() != null) {
            payment.setExchangeRate(scale8(request.getExchangeRate()));
            recalcBase = true;
        }

        if (request.getBaseCurrencyAmount() != null) {
            payment.setAmountBaseCurrency(scale4(request.getBaseCurrencyAmount()));
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

        // Note is only in DTOs, not entity — you included it in response, but not in
        // entity.
        // If you intended to persist note, add a `note` column in Payment. For now we
        // ignore request.getNote().

        payment.setUpdatedBy(user.getId());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentResponse getPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByReference(PaymentSourceType referenceType, Long referenceId) {
        return paymentRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId)
                .stream()
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
                .amount(payment.getAmountTxnCurrency())
                .currencyCode(payment.getCurrencyCode())
                .exchangeRate(payment.getExchangeRate())
                .baseCurrencyAmount(payment.getAmountBaseCurrency())
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
                .message("Payment processed successfully");

        if (payment.getPaymentMetadata() != null) {
            try {
                JSONObject meta = new JSONObject(payment.getPaymentMetadata());
                builder.paymentMetadata(meta.toMap());
            } catch (Exception e) {
                // Non-JSON metadata — omit map but keep system stable
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
        // entity uses (18,4) for base amount → scale 4
        return amount.multiply(rate).setScale(4, RoundingMode.HALF_UP);
    }
}
