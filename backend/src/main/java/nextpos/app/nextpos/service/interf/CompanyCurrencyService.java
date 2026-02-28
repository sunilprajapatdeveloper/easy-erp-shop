package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyCurrencyResponse;

import java.util.List;

public interface CompanyCurrencyService {

    CompanyCurrencyResponse createCompanyCurrency(CreateCompanyCurrencyRequest request);

    CompanyCurrencyResponse getCompanyCurrency(Long id);

    List<CompanyCurrencyResponse> listCompanyCurrencies();

    CompanyCurrencyResponse updateCompanyCurrency(Long id, UpdateCompanyCurrencyRequest request);

    void deleteCompanyCurrency(Long id);
}
