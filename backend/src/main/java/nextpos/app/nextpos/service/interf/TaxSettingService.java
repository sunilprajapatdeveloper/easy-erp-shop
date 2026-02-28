package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;

import java.util.List;

public interface TaxSettingService {

    TaxSettingResponse createTaxSetting(CreateTaxSettingRequest request);

    TaxSettingResponse getTaxSetting(Long id);

    List<TaxSettingResponse> listTaxSettings();

    TaxSettingResponse updateTaxSetting(Long id, UpdateTaxSettingRequest request);

    void deleteTaxSetting(Long id);
}