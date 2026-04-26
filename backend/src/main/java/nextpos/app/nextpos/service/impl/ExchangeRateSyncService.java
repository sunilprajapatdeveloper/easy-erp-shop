package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.response.ExternalRateResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.ExchangeRate;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ExchangeRateRepository;
import nextpos.app.nextpos.service.interf.ExchangeRateProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateSyncService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final List<ExchangeRateProvider> providers;

    @Value("${exchange.provider.name:FRANKFURTER}")
    private String preferredProviderName;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Synchronize global exchange rates using the configured provider.
     * Can be triggered by a scheduled job or manually.
     */
    @Transactional
    public void syncGlobalRates() {
        log.info("Starting global exchange rate synchronization");

        // Get all active currencies from DB
        List<Currency> allCurrencies = currencyRepository.findAll();
        if (allCurrencies.size() < 2) {
            log.warn("Not enough currencies to sync rates (minimum 2)");
            return;
        }

        // Map currency code -> Currency entity
        Map<String, Currency> currencyMap = allCurrencies.stream()
                .collect(Collectors.toMap(Currency::getCode, c -> c));

        // Select provider based on configuration
        ExchangeRateProvider provider = selectProvider();
        if (provider == null) {
            log.error("No exchange rate provider available");
            return;
        }

        final String providerName = provider.getProviderName(); // capture for use inside lambda
        log.info("Using provider: {} (configured: {})", providerName, preferredProviderName);

        // For each currency as base, fetch rates to all others (parallel)
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Currency baseCurrency : allCurrencies) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    ExternalRateResponse rateResponse = provider.getRates(baseCurrency.getCode());
                    processRates(baseCurrency, rateResponse, currencyMap, providerName);
                } catch (Exception e) {
                    log.error("Failed to fetch rates for base {}: {}", baseCurrency.getCode(), e.getMessage(), e);
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("Global exchange rate synchronization completed");
    }

    /**
     * Process the rates from the external API and upsert global exchange rates.
     */
    private void processRates(Currency baseCurrency, ExternalRateResponse response, Map<String, Currency> currencyMap,
            String providerName) {
        if (response.getRates() == null || response.getRates().isEmpty()) {
            log.warn("No rates returned for base {}", baseCurrency.getCode());
            return;
        }

        for (Map.Entry<String, BigDecimal> entry : response.getRates().entrySet()) {
            String targetCode = entry.getKey();
            BigDecimal rate = entry.getValue();

            Currency targetCurrency = currencyMap.get(targetCode);
            if (targetCurrency == null) {
                log.debug("Target currency {} not found in DB, skipping", targetCode);
                continue;
            }

            // Check if a global rate already exists for this pair
            Optional<ExchangeRate> existing = exchangeRateRepository
                    .findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                            baseCurrency, targetCurrency, ExchangeRateLevel.GLOBAL, null, null);

            if (existing.isPresent()) {
                // Update if rate changed
                ExchangeRate ex = existing.get();
                if (ex.getRate().compareTo(rate) != 0) {
                    ex.setRate(rate);
                    ex.setUpdatedAt(Instant.now());
                    exchangeRateRepository.save(ex);
                    log.debug("Updated global rate {} -> {} : {}", baseCurrency.getCode(), targetCode, rate);
                }
            } else {
                // Create new
                ExchangeRate newRate = ExchangeRate.builder()
                        .baseCurrency(baseCurrency)
                        .targetCurrency(targetCurrency)
                        .rate(rate)
                        .level(ExchangeRateLevel.GLOBAL)
                        .rateSource(providerName)
                        .validFrom(Instant.now())
                        .isManualOverride(false)
                        .build();
                exchangeRateRepository.save(newRate);
                log.debug("Created global rate {} -> {} : {}", baseCurrency.getCode(), targetCode, rate);
            }
        }
    }

    /**
     * Selects the exchange rate provider based on the configured name.
     * Falls back to the first working provider if the preferred one fails.
     */
    private ExchangeRateProvider selectProvider() {
        ExchangeRateProvider preferred = null;

        // First try to find the preferred provider by name
        for (ExchangeRateProvider provider : providers) {
            if (provider.getProviderName().equalsIgnoreCase(preferredProviderName)) {
                preferred = provider;
                break;
            }
        }

        if (preferred != null) {
            log.info("Using preferred provider: {}", preferred.getProviderName());
            // Optional: test that it works (e.g., fetch a test rate)
            try {
                preferred.getRates("EUR");
                return preferred;
            } catch (Exception e) {
                log.warn("Preferred provider {} failed test: {}", preferred.getProviderName(), e.getMessage());
                // Fall through to fallback
            }
        } else {
            log.warn("Preferred provider {} not found in the list", preferredProviderName);
        }

        // Fallback: return the first working provider
        for (ExchangeRateProvider provider : providers) {
            try {
                provider.getRates("EUR");
                log.info("Using fallback provider: {}", provider.getProviderName());
                return provider;
            } catch (Exception e) {
                log.warn("Provider {} failed test: {}", provider.getProviderName(), e.getMessage());
            }
        }

        return null;
    }
}