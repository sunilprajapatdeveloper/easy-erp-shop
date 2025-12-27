package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Request DTO for creating a new ExchangeRate
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateExchangeRateRequest {

    @NotNull(message = "Base currency ID is required")
    private Long baseCurrencyId;

    @NotNull(message = "Target currency ID is required")
    private Long targetCurrencyId;

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.00000001", inclusive = true, message = "Rate must be greater than 0")
    private BigDecimal rate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Bid rate must be greater than or equal to 0")
    private BigDecimal bidRate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Ask rate must be greater than or equal to 0")
    private BigDecimal askRate;

    @NotNull(message = "Exchange rate level is required")
    private ExchangeRateLevel level;

    /**
     * Optional company ID. Required if level is COMPANY or WAREHOUSE
     */
    private Long companyId;

    /**
     * Optional warehouse ID. Required if level is WAREHOUSE
     */
    private Long warehouseId;

    @NotNull(message = "Rate source is required")
    @Size(max = 50, message = "Rate source cannot exceed 50 characters")
    private String rateSource;

    @Size(max = 100, message = "Provider name cannot exceed 100 characters")
    private String providerName;

    @Size(max = 100, message = "Provider reference ID cannot exceed 100 characters")
    private String providerReferenceId;

    @DecimalMin(value = "0.0", inclusive = true, message = "Spread percentage must be greater than or equal to 0")
    private BigDecimal spreadPercentage;

    @Builder.Default
    private Boolean isManualOverride = false;

    @Size(max = 255, message = "Override reason cannot exceed 255 characters")
    private String overrideReason;

    @NotNull(message = "Valid from date is required")
    private Instant validFrom;

    /**
     * Optional: expiry date
     */
    private Instant validTo;
}