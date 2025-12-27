package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.OnlineOrderingSettingsResponse;

public interface OnlineOrderingSettingsService {

    OnlineOrderingSettingsResponse createOnlineOrderingSettings(CreateOnlineOrderingSettingsRequest request,
            Long createdBy);

    OnlineOrderingSettingsResponse updateOnlineOrderingSettings(UpdateOnlineOrderingSettingsRequest request,
            Long updatedBy);

    OnlineOrderingSettingsResponse getOnlineOrderingSettings(Long companyId);

    void deleteOnlineOrderingSettings(Long companyId, Long deletedBy);
}