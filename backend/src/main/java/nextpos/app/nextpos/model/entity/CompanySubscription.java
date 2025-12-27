package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nextpos.app.nextpos.model.enums.SubscriptionStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_subscriptions", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "company_id" })
}, indexes = {
                @Index(name = "idx_company_subscription_company", columnList = "company_id"),
                @Index(name = "idx_company_subscription_plan", columnList = "plan_id"),
                @Index(name = "idx_company_subscription_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "company", "subscriptionPlan" })
@EntityListeners(AuditingEntityListener.class)
public class CompanySubscription {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        /** Company owning this subscription (1-to-1). */
        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "company_id", nullable = false, unique = true)
        private Company company;

        /** The subscription plan assigned to the company. */
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "plan_id", nullable = false)
        private SubscriptionPlan subscriptionPlan;

        /** Subscription lifecycle fields. */
        @Column(name = "start_date", nullable = false)
        private LocalDateTime startDate;

        @Column(name = "end_date")
        private LocalDateTime endDate;

        /** Next billing date (renewal or invoice generation). */
        @Column(name = "next_billing_date")
        private LocalDateTime nextBillingDate;

        @Column(name = "renewal_date")
        private LocalDateTime renewalDate;

        /** Auto-renew flag. */
        @Column(name = "auto_renew", nullable = false)
        @Builder.Default
        private boolean autoRenew = true;

        /** Trial details. */
        @Column(name = "trial_active", nullable = false)
        @Builder.Default
        private boolean trialActive = false;

        @Column(name = "trial_end_date")
        private LocalDateTime trialEndDate;

        /** Subscription status (ACTIVE, EXPIRED, CANCELLED, TRIAL, etc.). */
        @Column(name = "status", nullable = false, length = 20)
        @Builder.Default
        private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

        /** Billing / payment reference (e.g., invoice or gateway subscription id). */
        @Column(name = "billing_reference", length = 100)
        private String billingReference;

        /** Audit fields. */
        @Column(name = "created_by", updatable = false)
        private Long createdBy;

        @CreatedDate
        @Column(name = "created_at", updatable = false, nullable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_by")
        private Long updatedBy;

        @LastModifiedDate
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        /** Soft delete flag. */
        @Column(name = "is_deleted", nullable = false)
        @Builder.Default
        private boolean isDeleted = false;

        /** Optimistic locking for concurrency control. */
        @Version
        private Long version;

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