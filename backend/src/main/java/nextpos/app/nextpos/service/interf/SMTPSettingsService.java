package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SMTPSettingsResponse;

public interface SMTPSettingsService {

    SMTPSettingsResponse createOrUpdateSMTPSettings(CreateSMTPSettingsRequest request);

    SMTPSettingsResponse updateSMTPSettings(Long companyId, UpdateSMTPSettingsRequest request);

    SMTPSettingsResponse getSMTPSettingsByCompanyId(Long companyId);

    void deleteSMTPSettings(Long companyId);

    boolean testConnection(Long companyId);

    void refreshCache(Long companyId);
}