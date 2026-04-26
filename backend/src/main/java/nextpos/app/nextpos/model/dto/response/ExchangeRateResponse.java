package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import nextpos.app.nextpos.model.enums.ExternalExchangeRateProvider;
import nextpos.app.nextpos.model.enums.ExchangeRateSource;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for ExchangeRate
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateResponse {

    private Long id;

    private CurrencyInfo baseCurrency;

    private CurrencyInfo targetCurrency;

    private BigDecimal rate;

    private BigDecimal bidRate;

    private BigDecimal askRate;

    private BigDecimal spreadPercentage;

    private ExchangeRateLevel level;

    private CompanyInfo company;

    private WarehouseInfo warehouse;

    private Boolean isManualOverride;

    private String overrideReason;

    private ExchangeRateSource rateSource;

    private ExternalExchangeRateProvider providerName;

    private String providerReferenceId;

    private Instant validFrom;

    private Instant validTo;

    private Instant createdAt;

    private Instant updatedAt;

    private Long version;

    /**
     * Nested DTO for Currency info
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CurrencyInfo {
        private Long id;
        private String code;
        private String symbol;
        private String name;
    }

    /**
     * Nested DTO for Company info
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompanyInfo {
        private Long id;
        private String name;
    }

    /**
     * Nested DTO for Warehouse info
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WarehouseInfo {
        private Long id;
        private String name;
    }
}