package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanySubscriptionRequest {

    /** Company ID to assign this subscription to. */
    @NotNull(message = "Company ID is required")
    private Long companyId;

    /** Subscription plan ID chosen by the company. */
    @NotNull(message = "Subscription plan ID is required")
    private Long subscriptionPlanId;

    /** Subscription lifecycle dates. */
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    /** Next billing date (renewal or invoice generation). */
    private LocalDateTime nextBillingDate;

    private LocalDateTime renewalDate;

    /** Auto-renew flag (default true). */
    @Builder.Default
    private Boolean autoRenew =  Boolean.TRUE;

    /** Trial details. */
    @Builder.Default
    private Boolean trialActive =  Boolean.TRUE;

    private LocalDateTime trialEndDate;

    /** Billing / payment reference (optional). */
    @Size(max = 100, message = "Billing reference cannot exceed 100 characters")
    private String billingReference;

    /** Created by user ID. */
    @NotNull(message = "Created by is required")
    private Long createdBy;
}