package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.model.enums.PlanStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Request DTO for updating an existing Subscription Plan.
 * Supports partial updates and enterprise-level consistency.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubscriptionPlanRequest {

    @Size(max = 100, message = "Plan name must not exceed 100 characters")
    private String name;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String currency;

    private BillingCycle billingCycle;

    private Boolean trialAvailable;

    @Min(value = 0, message = "Trial days cannot be negative")
    private Integer trialDays;

    @Min(value = 0, message = "Max users cannot be negative")
    private Integer maxUsers;

    @Min(value = 0, message = "Max branches cannot be negative")
    private Integer maxBranches;

    /**
     * Feature toggles for the plan.
     * Nullable to allow partial updates.
     */
    private Map<String, String> features;

    /**
     * List of regions/countries where the plan is available.
     * Nullable to allow partial updates.
     */
    private List<String> availableRegions;

    private PlanStatus status;

    /**
     * User performing the update (for auditing).
     */
    private Long updatedBy;
}
