package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.OnlineOrderingSettingsResponse;

public interface OnlineOrderingSettingsService {

        OnlineOrderingSettingsResponse createOnlineOrderingSettings(CreateOnlineOrderingSettingsRequest request);

        OnlineOrderingSettingsResponse updateOnlineOrderingSettings(UpdateOnlineOrderingSettingsRequest request);

        OnlineOrderingSettingsResponse getOnlineOrderingSettings();

        void deleteOnlineOrderingSettings();
}