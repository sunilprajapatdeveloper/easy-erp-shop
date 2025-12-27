package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.BillingCycle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubscriptionPlanRequest {

    @NotBlank(message = "Plan name is required")
    @Size(max = 100, message = "Plan name must not exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "Currency code is required")
    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String currency;

    @NotNull(message = "Billing cycle is required")
    private BillingCycle billingCycle;

    @Builder.Default
    private boolean trialAvailable = false;

    @Builder.Default
    @Min(value = 0, message = "Trial days cannot be negative")
    private int trialDays = 0;

    @Min(value = 0, message = "Max users cannot be negative")
    private Integer maxUsers;

    @Min(value = 0, message = "Max branches cannot be negative")
    private Integer maxBranches;

    @Builder.Default
    private Map<String, String> features = Map.of();

    @Builder.Default
    private List<String> availableRegions = List.of();
}
