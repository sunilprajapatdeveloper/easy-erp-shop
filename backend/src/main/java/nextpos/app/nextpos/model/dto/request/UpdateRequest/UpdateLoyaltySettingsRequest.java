package nextpos.app.nextpos.model.dto.request.UpdateRequest;

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
public class UpdateLoyaltySettingsRequest {

    private Boolean enabled;

    private String programName;

    private String loyaltyType;

    private BigDecimal pointsPerCurrency;

    private BigDecimal currencyPerPoint;

    private Integer pointsExpiryDays;

    private BigDecimal cashbackPercentage;

    private BigDecimal minOrderAmountForCashback;

    private Map<String, Object> tierRules;

    private Integer minPointsToRedeem;

    private BigDecimal maxDiscountPercentage;

    private Map<String, Object> extraSettings;

    private Boolean isActive;
}