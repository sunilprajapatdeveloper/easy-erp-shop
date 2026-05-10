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

import nextpos.app.nextpos.model.enums.PaymentAttemptType;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.model.enums.PaymentStatus;

@Entity
@Table(name = "payment_transaction_logs", indexes = {
        @Index(name = "idx_payment_log_payment", columnList = "payment_id"),
        @Index(name = "idx_payment_log_company", columnList = "company_id"),
        @Index(name = "idx_payment_log_status", columnList = "status"),
        @Index(name = "idx_payment_log_trace", columnList = "trace_id")
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
    private PaymentGatewayProvider gatewayProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "attempt_type", length = 30)
    private PaymentAttemptType attemptType;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload;

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "trace_id", length = 100)
    private String traceId;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "executed_by")
    private Long executedBy;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
