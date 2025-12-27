package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyCurrencyResponse;

import java.util.List;

public interface CompanyCurrencyService {

    CompanyCurrencyResponse createCompanyCurrency(Long companyId, CreateCompanyCurrencyRequest request);

    CompanyCurrencyResponse getCompanyCurrency(Long id, Long companyId);

    List<CompanyCurrencyResponse> listCompanyCurrencies(Long companyId);

    CompanyCurrencyResponse updateCompanyCurrency(Long id, Long companyId, UpdateCompanyCurrencyRequest request);

    void deleteCompanyCurrency(Long id, Long companyId);
}
