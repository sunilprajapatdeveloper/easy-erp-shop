package nextpos.app.nextpos.service.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import nextpos.app.nextpos.repository.CompanyCurrencyRepository;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ExchangeRateRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverter {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final CompanyCurrencyRepository companyCurrencyRepository;

    /**
     * Convert amount from source currency to target currency for a given company
     * and warehouse.
     * The rate is resolved with the following priority:
     * 1. WAREHOUSE level (if warehouseId provided)
     * 2. COMPANY level (if companyId provided)
     * 3. SYSTEM level (if no specific level found)
     *
     * @param amount         amount to convert (not null)
     * @param companyId      tenant company ID
     * @param warehouseId    specific warehouse (can be null)
     * @param sourceCurrency source currency code (e.g., "USD")
     * @param targetCurrency target currency code (e.g., "INR")
     * @return converted amount scaled to 2 decimal places
     * @throws IllegalArgumentException if no suitable exchange rate found
     */
    public BigDecimal convert(BigDecimal amount, Long companyId, Long warehouseId,
            String sourceCurrency, String targetCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (sourceCurrency.equalsIgnoreCase(targetCurrency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        Currency baseCurr = getCurrencyByCode(sourceCurrency);
        Currency targetCurr = getCurrencyByCode(targetCurrency);
        Company company = companyId != null ? Company.builder().id(companyId).build() : null;
        Warehouse warehouse = warehouseId != null ? Warehouse.builder().id(warehouseId).build() : null;

        // Try WAREHOUSE level first
        ExchangeRate rate = findExchangeRate(baseCurr, targetCurr, ExchangeRateLevel.WAREHOUSE, company, warehouse);
        if (rate == null && company != null) {
            // Try COMPANY level
            rate = findExchangeRate(baseCurr, targetCurr, ExchangeRateLevel.COMPANY, company, null);
        }
        if (rate == null) {
            // Try SYSTEM level
            rate = findExchangeRate(baseCurr, targetCurr, ExchangeRateLevel.GLOBAL, null, null);
        }
        if (rate == null) {
            throw new IllegalArgumentException(
                    String.format("No exchange rate found for %s -> %s for company %d, warehouse %d",
                            sourceCurrency, targetCurrency, companyId, warehouseId));
        }

        BigDecimal converted = amount.multiply(rate.getRate());
        return converted.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Convert from transaction currency to company's base (default) currency.
     *
     * @param amount              amount in transaction currency
     * @param companyId           company ID
     * @param warehouseId         warehouse ID (optional, used for rate resolution)
     * @param transactionCurrency currency code of the transaction
     * @return amount in base currency
     */
    public BigDecimal toBaseCurrency(BigDecimal amount, Long companyId, Long warehouseId, String transactionCurrency) {
        String baseCurrencyCode = getBaseCurrencyCode(companyId);
        return convert(amount, companyId, warehouseId, transactionCurrency, baseCurrencyCode);
    }

    /**
     * Convert from company base currency to transaction currency.
     *
     * @param amount              amount in base currency
     * @param companyId           company ID
     * @param warehouseId         warehouse ID (optional)
     * @param transactionCurrency target transaction currency
     * @return amount in transaction currency
     */
    public BigDecimal fromBaseCurrency(BigDecimal amount, Long companyId, Long warehouseId,
            String transactionCurrency) {
        String baseCurrencyCode = getBaseCurrencyCode(companyId);
        return convert(amount, companyId, warehouseId, baseCurrencyCode, transactionCurrency);
    }

    /**
     * Simple conversion using only company level (warehouse ignored) – useful for
     * promotions that are global.
     * Delegates to convert() with warehouseId = null.
     */
    public BigDecimal convertCompanyLevel(BigDecimal amount, Long companyId, String sourceCurrency,
            String targetCurrency) {
        return convert(amount, companyId, null, sourceCurrency, targetCurrency);
    }

    private Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + code));
    }

    private ExchangeRate findExchangeRate(Currency base, Currency target, ExchangeRateLevel level,
            Company company, Warehouse warehouse) {
        Optional<ExchangeRate> rateOpt = exchangeRateRepository
                .findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                        base, target, level, company, warehouse);
        if (rateOpt.isPresent()) {
            ExchangeRate rate = rateOpt.get();
            Instant now = Instant.now();
            if (rate.getValidFrom().isBefore(now) && (rate.getValidTo() == null || rate.getValidTo().isAfter(now))) {
                return rate;
            }
        }
        return null;
    }

    private String getBaseCurrencyCode(Long companyId) {
        return companyCurrencyRepository.findByCompanyId(companyId)
                .stream()
                .filter(CompanyCurrency::isDefaultCurrency)
                .findFirst()
                .map(cc -> cc.getCurrency().getCode())
                .orElseThrow(() -> new IllegalArgumentException("No default currency found for company " + companyId));
    }
}