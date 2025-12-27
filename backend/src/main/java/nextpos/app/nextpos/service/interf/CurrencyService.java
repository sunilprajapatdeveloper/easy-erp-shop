package nextpos.app.nextpos.service.interf;

// import nextpos.app.nextpos.model.dto.request.CreateCurrencyRequest;
// import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CurrencyResponse;

import java.util.List;

public interface CurrencyService {
    // CurrencyResponse createCurrency(CreateCurrencyRequest request);
    CurrencyResponse getCurrencyById(Long id);
    // List<CurrencyResponse> findAllByCreatedBy(Long id);
    List<CurrencyResponse> getAllCurrencies();
    // CurrencyResponse updateCurrency(Long id, UpdateCurrencyRequest request);
    // void deleteCurrency(Long id);
}