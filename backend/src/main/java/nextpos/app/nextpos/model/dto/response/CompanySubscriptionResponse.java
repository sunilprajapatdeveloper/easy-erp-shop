package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

/**
 * Response DTO for CompanySubscription.
 * Represents the subscription details of a company,
 * including the plan, status, and lifecycle metadata.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySubscriptionResponse {

    private Long id;

    /** Owning company */
    private Long companyId;

    /** The subscription plan assigned */
    private Long subscriptionPlanId;

    /** Full subscribed plan details */
    private SubscriptionPlanResponse plan;

    /** Subscription lifecycle fields */
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextBillingDate;
    private LocalDateTime renewalDate;

    /** Trial details */
    private Boolean trialActive;
    private LocalDateTime trialEndDate;

    /** Auto-renew flag */
    private Boolean autoRenew;

    /** Current subscription status */
    private SubscriptionStatus status;

    /** Billing / payment reference (e.g., invoice id) */
    private String billingReference;

    /** Auditing fields */
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;

    /** Soft delete flag */
    private Boolean isDeleted;

    /** Version for optimistic locking */
    private Long version;
}
