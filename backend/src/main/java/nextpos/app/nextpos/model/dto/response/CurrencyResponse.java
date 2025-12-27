package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Currency;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CurrencyResponse {
    private Long id;
    private String name;
    private String code;
    private String symbol;
    // private BigDecimal exchangeRate;
    // private Integer decimalPlaces;
    // private String roundingMode;
    // private Boolean active;
    // private Boolean isBaseCurrency;
    // private Long createdBy;
    // private LocalDateTime createdAt;
    // private Long updatedBy;
    // private LocalDateTime updatedAt;
    // private Long companyId;

    public CurrencyResponse(Currency currency) {
        this.id = currency.getId();
        this.name = currency.getName();
        this.code = currency.getCode();
        this.symbol = currency.getSymbol();
        // this.exchangeRate = currency.getExchangeRate();
        // this.decimalPlaces = currency.getDecimalPlaces();
        // this.roundingMode = currency.getRoundingMode();
        // this.active = currency.isActive();
        // this.isBaseCurrency = currency.isBaseCurrency();
        // this.createdBy = currency.getCreatedBy();
        // this.createdAt = currency.getCreatedAt();
        // this.updatedBy = currency.getUpdatedBy();
        // this.updatedAt = currency.getUpdatedAt();
        // this.companyId = currency.getCompanyId();
    }
}
