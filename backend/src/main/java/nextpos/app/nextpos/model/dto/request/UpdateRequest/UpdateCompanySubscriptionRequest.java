package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompanySubscriptionRequest {

    /** Update subscription plan (e.g., upgrade/downgrade). */
    private Long subscriptionPlanId;

    /** Subscription lifecycle dates. */
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    /** Next billing date (renewal or invoice generation). */
    private LocalDateTime nextBillingDate;

    private LocalDateTime renewalDate;

    /** Auto-renew flag. */
    private Boolean autoRenew;

    private SubscriptionStatus status;

    /** Trial details. */
    private Boolean trialActive;

    private LocalDateTime trialEndDate;

    /** Billing / payment reference (optional). */
    @Size(max = 100, message = "Billing reference cannot exceed 100 characters")
    private String billingReference;

    /** Updated by user ID. */
    @NotNull(message = "Updated by is required")
    private Long updatedBy;
}