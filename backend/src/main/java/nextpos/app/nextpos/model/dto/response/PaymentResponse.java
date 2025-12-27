package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.model.enums.PaymentType;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentSourceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;

    private String referenceNumber;

    private PaymentSourceType referenceType; // SALE, PURCHASE, SALE_RETURN, PURCHASE_RETURN

    private Long referenceId;

    private PaymentType paymentType; // INCOMING / OUTGOING

    private BigDecimal amount; // Amount in transaction currency

    private PaymentMethod paymentMethod; // CASH, CARD, UPI, BANK_TRANSFER, etc.

    private PaymentGatewayProvider gatewayProvider; // Optional, e.g., STRIPE, RAZORPAY

    private String transactionReference; // External txn ID (bank, POS, gateway)

    private PaymentStatus status; // PENDING, SUCCESS, FAILED, PAID, REFUNDED, etc.

    private LocalDate paymentDate;

    private String note; // Optional human-readable note

    private String message; // Success / failure message

    /**
     * ISO 4217 currency code (e.g., USD, EUR, INR).
     */
    private String currencyCode;

    /**
     * Exchange rate relative to the company’s base currency.
     * Example: if company base = INR, payment = USD, and 1 USD = 83 INR →
     * exchangeRate = 83.
     */
    private BigDecimal exchangeRate;

    /**
     * Converted amount in company base currency.
     * (amount * exchangeRate)
     */
    private BigDecimal baseCurrencyAmount;

    private String idempotencyKey; // Optional for idempotent processing

    private Map<String, Object> paymentMetadata; // JSON metadata (card last4, UPI id, etc.)

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;

    private Long companyId;
}
