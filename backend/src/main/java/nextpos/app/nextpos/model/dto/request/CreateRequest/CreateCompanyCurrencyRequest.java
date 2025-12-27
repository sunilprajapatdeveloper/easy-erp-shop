package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.CurrencyStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyCurrencyRequest {

    @NotNull(message = "Currency ID is required")
    private Long currencyId;

    @Min(value = 0, message = "Decimal places must be zero or greater")
    private Integer decimalPlaces;

    @Builder.Default
    private boolean defaultCurrency = false;

    @NotNull(message = "Currency status is required")
    private CurrencyStatus status;
}
