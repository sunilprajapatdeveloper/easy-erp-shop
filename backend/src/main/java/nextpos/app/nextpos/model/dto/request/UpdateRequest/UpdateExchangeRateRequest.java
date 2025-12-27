package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
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
 * Request DTO for updating an existing ExchangeRate
 * Partial updates are allowed; only fields provided will be updated
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateExchangeRateRequest {

    /**
     * Optional new rate. Must be greater than 0 if provided
     */
    @DecimalMin(value = "0.00000001", inclusive = true, message = "Rate must be greater than 0")
    private BigDecimal rate;

    /**
     * Optional bid rate. Must be >= 0 if provided
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Bid rate must be greater than or equal to 0")
    private BigDecimal bidRate;

    /**
     * Optional ask rate. Must be >= 0 if provided
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Ask rate must be greater than or equal to 0")
    private BigDecimal askRate;

    /**
     * Optional override: allows updating the exchange rate level (GLOBAL, COMPANY,
     * WAREHOUSE)
     */
    private ExchangeRateLevel level;

    /**
     * Optional company ID (required if level is COMPANY or WAREHOUSE)
     */
    private Long companyId;

    /**
     * Optional warehouse ID (required if level is WAREHOUSE)
     */
    private Long warehouseId;

    /**
     * Optional rate source (max 50 characters)
     */
    @Size(max = 50, message = "Rate source cannot exceed 50 characters")
    private String rateSource;

    /**
     * Optional provider name (max 100 characters)
     */
    @Size(max = 100, message = "Provider name cannot exceed 100 characters")
    private String providerName;

    /**
     * Optional provider reference ID (max 100 characters)
     */
    @Size(max = 100, message = "Provider reference ID cannot exceed 100 characters")
    private String providerReferenceId;

    /**
     * Optional spread percentage
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Spread percentage must be greater than or equal to 0")
    private BigDecimal spreadPercentage;

    /**
     * Optional flag to indicate if this is a manual override
     */
    private Boolean isManualOverride;

    /**
     * Optional reason for override (max 255 characters)
     */
    @Size(max = 255, message = "Override reason cannot exceed 255 characters")
    private String overrideReason;

    /**
     * Optional valid from date
     */
    private Instant validFrom;

    /**
     * Optional valid to (expiry) date
     */
    private Instant validTo;
}