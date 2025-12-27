package nextpos.app.nextpos.strategy;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.PaymentProcessingException;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class GiftCardPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public GiftCardPaymentStrategy(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.GIFT_CARD.name().equalsIgnoreCase(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentResponse pay(CreatePaymentRequest request) {
        log.info("Processing gift card payment: referenceType={}, referenceId={}, amount={}",
                request.getReferenceType(), request.getReferenceId(), request.getAmount());

        try {
            // Get authenticated user
            User user = UserContext.getAuthenticatedUser(userRepository);

            // Compute base currency amount if not provided
            var baseCurrencyAmount = request.getBaseCurrencyAmount() != null
                    ? request.getBaseCurrencyAmount()
                    : request.getAmount().multiply(request.getExchangeRate());

            // Save payment
            Payment payment = Payment.builder()
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .referenceNumber(request.getReferenceNumber())
                    .paymentType(request.getPaymentType())
                    .amountTxnCurrency(request.getAmount())
                    .amountBaseCurrency(baseCurrencyAmount)
                    .currencyCode(request.getCurrencyCode())
                    .exchangeRate(request.getExchangeRate())
                    .paymentMethod(PaymentMethod.GIFT_CARD)
                    .status(PaymentStatus.PAID)
                    .paymentDate(request.getPaymentDate())
                    .transactionReference(request.getTransactionReference())
                    .idempotencyKey(request.getIdempotencyKey())
                    .createdBy(user != null ? user.getId() : null)
                    .updatedBy(user != null ? user.getId() : null)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .companyId(user != null ? user.getCompanyId() : 1L)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            log.info("Gift card payment successful: paymentId={}, referenceNumber={}",
                    savedPayment.getId(), savedPayment.getReferenceNumber());

            // Return response
            return PaymentResponse.builder()
                    .id(savedPayment.getId())
                    .referenceNumber(savedPayment.getReferenceNumber())
                    .referenceType(savedPayment.getReferenceType())
                    .referenceId(savedPayment.getReferenceId())
                    .paymentType(savedPayment.getPaymentType())
                    .amount(savedPayment.getAmountTxnCurrency())
                    .baseCurrencyAmount(savedPayment.getAmountBaseCurrency())
                    .currencyCode(savedPayment.getCurrencyCode())
                    .exchangeRate(savedPayment.getExchangeRate())
                    .paymentMethod(savedPayment.getPaymentMethod())
                    .status(savedPayment.getStatus())
                    .transactionReference(savedPayment.getTransactionReference())
                    .idempotencyKey(savedPayment.getIdempotencyKey())
                    .companyId(savedPayment.getCompanyId())
                    .createdBy(savedPayment.getCreatedBy())
                    .createdAt(savedPayment.getCreatedAt())
                    .updatedBy(savedPayment.getUpdatedBy())
                    .updatedAt(savedPayment.getUpdatedAt())
                    .message("Gift card payment successful.")
                    .build();

        } catch (Exception e) {
            log.error("Gift card payment failed: referenceId={}, error={}", request.getReferenceId(), e.getMessage(),
                    e);
            throw new PaymentProcessingException("Gift card payment failed: " + e.getMessage(), e);
        }
    }
}
