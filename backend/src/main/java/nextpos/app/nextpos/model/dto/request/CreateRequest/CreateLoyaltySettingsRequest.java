package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoyaltySettingsRequest {

    @NotBlank
    private String loyaltyType; // POINTS, CASHBACK, TIERED

    private Boolean enabled;

    private String programName;

    // Points system
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pointsPerCurrency;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal currencyPerPoint;

    private Integer pointsExpiryDays;

    // Cashback system
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal cashbackPercentage;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal minOrderAmountForCashback;

    // Tiered system (JSON rules)
    private Map<String, Object> tierRules;

    // Redemption rules
    private Integer minPointsToRedeem;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal maxDiscountPercentage;

    // Extra flexible JSON settings
    private Map<String, Object> extraSettings;
}