package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentRequest {

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount; // Transaction currency amount

    private PaymentMethod paymentMethod; // CASH, CARD, UPI, BANK_TRANSFER, etc.

    private PaymentGatewayProvider gatewayProvider; // Optional: STRIPE, PAYPAL, etc.

    private PaymentStatus status; // PENDING, SUCCESS, FAILED, PAID, REFUNDED, etc.

    private LocalDate paymentDate; // When payment was made

    private String note; // Optional human note

    private String transactionReference; // External txn ID (bank, POS slip, etc.)

    /**
     * ISO 4217 currency code (e.g., USD, EUR, INR).
     * Can be updated if the payment currency was entered incorrectly.
     */
    private String currencyCode;

    /**
     * Exchange rate relative to the company’s base currency.
     * Example: if company base = INR, payment = USD, and 1 USD = 83 INR →
     * exchangeRate = 83.
     */
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate;

    /**
     * Converted amount in company base currency.
     * (amount * exchangeRate). Optional — may be pre-computed or recalculated.
     */
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal baseCurrencyAmount;

    @Size(max = 100)
    private String idempotencyKey; // Optional safeguard for idempotent updates
}
