package nextpos.app.nextpos.service.impl.strategy;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.response.ExternalRateResponse;
import nextpos.app.nextpos.service.interf.ExchangeRateProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class FrankfurterProvider implements ExchangeRateProvider {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FrankfurterProvider(
            @Value("${exchange.provider.frankfurter.url:https://api.frankfurter.app}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ExternalRateResponse getRates(String baseCurrencyCode) {
        String url = baseUrl + "/latest?base=" + baseCurrencyCode;
        log.debug("Fetching rates from Frankfurter: {}", url);
        var response = restTemplate.getForObject(url, ExternalRateResponse.class);
        if (response == null) {
            throw new RuntimeException("Empty response from Frankfurter API");
        }
        return response;
    }

    @Override
    public String getProviderName() {
        return "FRANKFURTER";
    }

    @Override
    public boolean supportsArbitraryBase() {
        // Frankfurter supports any base currency
        return true;
    }
}