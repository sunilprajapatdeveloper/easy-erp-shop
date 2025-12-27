package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.response.SecuritySettingsResponse;

import java.util.List;

public interface SecuritySettingsService {

    SecuritySettingsResponse createSecuritySettings(CreateSecuritySettingsRequest request, Long companyId,
            Long createdBy);

    SecuritySettingsResponse updateSecuritySettings(Long companyId, UpdateSecuritySettingsRequest request,
            Long updatedBy);

    SecuritySettingsResponse getSecuritySettings(Long companyId);

    List<SecuritySettingsResponse> listAllSecuritySettings();
}