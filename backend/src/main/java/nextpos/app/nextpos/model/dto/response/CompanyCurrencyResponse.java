package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.CurrencyStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyCurrencyResponse {

    private Long id;

    private Long currencyId;
    private String currencyCode;
    private String currencyName;
    private String symbol;

    private Integer decimalPlaces;
    private boolean defaultCurrency;
    private CurrencyStatus status;

    private Long companyId;
}
