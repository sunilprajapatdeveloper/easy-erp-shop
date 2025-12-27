package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.CurrencyStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWarehouseCurrencyRequest {

    private Long currencyId;

    @Min(value = 0, message = "Decimal places must be zero or greater")
    private Integer decimalPlaces;

    private Boolean defaultCurrency;

    private CurrencyStatus status;
}
