package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.response.ExchangeRateResponse;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRateResponse createExchangeRate(CreateExchangeRateRequest request);

    ExchangeRateResponse updateExchangeRate(Long id, UpdateExchangeRateRequest request);

    ExchangeRateResponse getExchangeRate(Long id);

    void deleteExchangeRate(Long id);

    List<ExchangeRateResponse> getAllExchangeRates();

    // Optional: find by base/target currency & warehouse/company/global scope
    ExchangeRateResponse findExchangeRate(Long baseCurrencyId, Long targetCurrencyId, Long companyId, Long warehouseId);
}
