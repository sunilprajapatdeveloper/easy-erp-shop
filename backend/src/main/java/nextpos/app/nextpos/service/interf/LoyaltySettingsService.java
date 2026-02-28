package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.response.LoyaltySettingsResponse;

import java.util.List;

public interface LoyaltySettingsService {

    LoyaltySettingsResponse createLoyaltySettings(CreateLoyaltySettingsRequest request);

    LoyaltySettingsResponse updateLoyaltySettings(Long id, UpdateLoyaltySettingsRequest request);

    LoyaltySettingsResponse getLoyaltySettings(Long id);

    List<LoyaltySettingsResponse> listLoyaltySettings();
}
