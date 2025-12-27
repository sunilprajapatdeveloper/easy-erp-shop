package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;

import java.util.List;

public interface TaxSettingService {

    TaxSettingResponse createTaxSetting(CreateTaxSettingRequest request, Long companyId);

    TaxSettingResponse getTaxSetting(Long id, Long companyId);

    List<TaxSettingResponse> listTaxSettings(Long companyId);

    TaxSettingResponse updateTaxSetting(Long id, Long companyId, UpdateTaxSettingRequest request);

    void deleteTaxSetting(Long id, Long companyId);
}