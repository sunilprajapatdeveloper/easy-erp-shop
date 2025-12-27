package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;

@Entity
@Table(name = "payment_transaction_logs", indexes = {
        @Index(name = "idx_payment_log_payment", columnList = "payment_id"),
        @Index(name = "idx_payment_log_company", columnList = "company_id"),
        @Index(name = "idx_payment_log_success", columnList = "success")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to Payment entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "gateway_provider", length = 50)
    private PaymentGatewayProvider gatewayProvider; // STRIPE, RAZORPAY, etc.

    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload; // JSON request (masked if sensitive)

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload; // JSON response (masked if sensitive)

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "error_message", length = 1000)
    private String errorMessage; // Optional debug info

    @Column(name = "attempt_type", length = 50)
    private String attemptType; // INITIATED, CALLBACK, RETRY, etc.

    @Column(name = "trace_id", length = 100)
    private String traceId; // Optional correlation ID

    @Column(name = "executed_by")
    private Long executedBy; // User ID performing the action

    @Column(name = "company_id", nullable = false)
    private Long companyId; // Same as Payment.companyId

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
