package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ExchangeRateSource;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentMethod;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.model.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_reference", columnList = "reference_type, reference_id"),
        @Index(name = "idx_payment_company", columnList = "company_id"),
        @Index(name = "idx_payment_status", columnList = "status"),
        @Index(name = "idx_payment_date", columnList = "payment_date"),
        @Index(name = "idx_payments_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_payments_pos_terminal", columnList = "pos_terminal_id"),
        @Index(name = "idx_payments_ref_currency", columnList = "reference_currency_code")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_payment_idempotency", columnNames = { "idempotency_key", "company_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SALE, SALE_RETURN, PURCHASE, PURCHASE_RETURN
    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false, length = 30)
    private PaymentSourceType referenceType;

    // Entity ID (Sale.id, Purchase.id, etc.)
    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    // Human-readable reference (original sale reference, purchase reference, etc)
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    // INCOMING (customer pays) / OUTGOING (we pay vendor)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    // Amount in transaction currency (what was actually paid)
    @Column(name = "amount_txn_currency", nullable = false, precision = 18, scale = 4)
    private BigDecimal amountTxnCurrency;

    // Currency of the payment (e.g., USD, EUR)
    @Column(name = "currency_code", length = 10, nullable = false)
    private String currencyCode;

    // Exchange rate used to convert amountTxnCurrency to company base currency
    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    // Value in company's functional currency (for GL)
    @Column(name = "amount_base_currency", nullable = false, precision = 18, scale = 4)
    private BigDecimal amountBaseCurrency;

    // Currency of the original document (e.g., sale currency, purchase currency)
    @Column(name = "reference_currency_code", length = 10)
    private String referenceCurrencyCode;

    // Total amount in the document's currency
    @Column(name = "reference_amount", precision = 18, scale = 4)
    private BigDecimal referenceAmount;

    // Warehouse where transaction occurred
    @Column(name = "warehouse_id")
    private Long warehouseId;

    // POS terminal identifier (if applicable)
    @Column(name = "pos_terminal_id", length = 100)
    private String posTerminalId;

    // Source of exchange rate (e.g., "OPENEXCHANGERATES", "MANUAL", "STRIPE")
    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_rate_source", length = 50)
    private ExchangeRateSource exchangeRateSource;

    // CASH, CARD, UPI, BANK_TRANSFER, etc.
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    // STRIPE, PAYPAL, RAZORPAY, etc.
    @Enumerated(EnumType.STRING)
    @Column(name = "gateway_provider", length = 50)
    private PaymentGatewayProvider gatewayProvider;

    // PENDING, SUCCESS, FAILED, REFUNDED, PARTIALLY_REFUNDED
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    // Transaction ID provided by the payment gateway
    @Column(name = "transaction_reference", length = 150)
    private String transactionReference;

    // Unique reference to prevent duplicate payment processing
    @Column(name = "idempotency_key", length = 100, nullable = false)
    private String idempotencyKey;

    // JSON: { "card_last4": "1234", "upi_id": "...", "ip": "..." }
    @Column(name = "payment_metadata", columnDefinition = "TEXT")
    private String paymentMetadata;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
