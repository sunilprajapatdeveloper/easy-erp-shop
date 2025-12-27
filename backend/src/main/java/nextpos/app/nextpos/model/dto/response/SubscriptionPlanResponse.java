package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.model.enums.PlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for SubscriptionPlan entity.
 * Provides a full enterprise-level view of subscription plan details.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanResponse {

    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private BillingCycle billingCycle;

    private boolean trialAvailable;
    private int trialDays;
    private Integer maxUsers;
    private Integer maxBranches;

    private Map<String, String> features;
    private List<String> availableRegions;

    private PlanStatus status;
    private boolean isDeleted;

    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long version;

    /**
     * Constructor to map from entity to response DTO.
     *
     * @param plan the SubscriptionPlan entity
     */
    public SubscriptionPlanResponse(SubscriptionPlan plan) {
        this.id = plan.getId();
        this.name = plan.getName();
        this.description = plan.getDescription();
        this.price = plan.getPrice();
        this.currency = plan.getCurrency();
        this.billingCycle = plan.getBillingCycle();
        this.trialAvailable = plan.isTrialAvailable();
        this.trialDays = plan.getTrialDays();
        this.maxUsers = plan.getMaxUsers();
        this.maxBranches = plan.getMaxBranches();
        this.features = plan.getFeatures();
        this.availableRegions = plan.getAvailableRegions();
        this.status = plan.getStatus();
        this.isDeleted = plan.isDeleted();
        this.createdBy = plan.getCreatedBy();
        this.updatedBy = plan.getUpdatedBy();
        this.createdAt = plan.getCreatedAt();
        this.updatedAt = plan.getUpdatedAt();
        this.version = plan.getVersion();
    }
}
