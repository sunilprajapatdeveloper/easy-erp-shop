package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.response.ExternalRateResponse;

public interface ExchangeRateProvider {
    /**
     * Fetch latest exchange rates for the given base currency.
     * Returns a map of target currency code -> rate.
     */
    ExternalRateResponse getRates(String baseCurrencyCode);

    /**
     * The base currency for which this provider gives rates.
     * Frankfurter uses EUR, but we can still request other bases.
     * Used for logging/metrics.
     */
    default String getPreferredBase() {
        return "EUR";
    }

    /**
     * The name of the provider (e.g., "FRANKFURTER")
     */
    String getProviderName();

    /**
     * Whether this provider supports fetching rates for any base currency.
     * Most free APIs only support EUR as base; we can compute cross rates.
     */
    default boolean supportsArbitraryBase() {
        return false;
    }
}