package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.PaymentType;
import nextpos.app.nextpos.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotNull
    private PaymentSourceType referenceType; // SALE, SALE_RETURN, PURCHASE, PURCHASE_RETURN, etc.

    private Long referenceId;

    private String referenceNumber; // Human-readable reference (e.g., sale ref, purchase ref)

    @NotNull
    private PaymentType paymentType; // INCOMING (customer) / OUTGOING (vendor)

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount; // Transaction currency amount

    @NotNull
    private PaymentMethod paymentMethod; // CASH, CARD, UPI, BANK_TRANSFER, etc.

    private PaymentGatewayProvider gatewayProvider; // Optional (STRIPE, RAZORPAY, etc.)

    private String paymentData; // JSON metadata or extra info (card_last4, upi_id, etc.)

    @NotNull
    private PaymentStatus status; // PENDING, SUCCESS, FAILED, PAID, REFUNDED, etc.

    @NotNull
    private LocalDate paymentDate; // When payment was made

    private String note; // Optional human note

    private String transactionReference; // External txn ID (bank, POS slip, etc.)

    @Size(max = 100)
    private String idempotencyKey; // To prevent duplicate processing

    @NotNull
    private String currencyCode; // ISO 4217 currency code (USD, INR, EUR)

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate; // Rate relative to company base currency

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal baseCurrencyAmount; // Optional: amount in company base currency
}
