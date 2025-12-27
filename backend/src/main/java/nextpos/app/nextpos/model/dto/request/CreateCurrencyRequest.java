package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class CreateCurrencyRequest {

    @NotBlank
    private final String name;

    @NotBlank
    @Size(min = 3, max = 3)
    private final String code;

    @NotBlank
    private final String symbol;

    /**
     * Exchange rate between this currency and company's base currency.
     * Must be positive. Required for non-base currencies.
     */
    @DecimalMin(value = "0.000001", inclusive = true)
    private final BigDecimal exchangeRate;

    /**
     * Number of decimal places for this currency.
     * Optional; default will be 2 if not provided.
     */
    private final Integer decimalPlaces;

    /**
     * Rounding mode, e.g., "HALF_UP", "DOWN", etc.
     * Optional; default handled in service if null.
     */
    private final String roundingMode;

    /**
     * Whether this currency is active.
     * Optional; defaults to true.
     */
    private final Boolean active;

    /**
     * Is this the company's base currency.
     * Optional; only one base currency per company.
     */
    private final Boolean isBaseCurrency;
}
