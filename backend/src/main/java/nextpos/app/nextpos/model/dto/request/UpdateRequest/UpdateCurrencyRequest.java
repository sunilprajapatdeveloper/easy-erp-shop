package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class UpdateCurrencyRequest {

    private final String name;

    @Size(min = 3, max = 3)
    private final String code;

    private final String symbol;

    /**
     * Exchange rate between this currency and company's base currency.
     * Must be positive if provided.
     */
    @DecimalMin(value = "0.000001", inclusive = true)
    private final BigDecimal exchangeRate;

    /**
     * Number of decimal places for this currency.
     */
    private final Integer decimalPlaces;

    /**
     * Rounding mode, e.g., "HALF_UP", "DOWN", etc.
     */
    private final String roundingMode;

    /**
     * Whether this currency is active.
     */
    private final Boolean active;

    /**
     * Whether this currency is the company's base currency.
     * Only one base currency allowed per company.
     */
    private final Boolean isBaseCurrency;
}
