package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.response.LoyaltySettingsResponse;

import java.util.List;

public interface LoyaltySettingsService {

    LoyaltySettingsResponse createLoyaltySettings(CreateLoyaltySettingsRequest request, Long companyId, Long createdBy);

    LoyaltySettingsResponse updateLoyaltySettings(Long id, Long companyId, UpdateLoyaltySettingsRequest request,
            Long updatedBy);

    LoyaltySettingsResponse getLoyaltySettings(Long id, Long companyId);

    List<LoyaltySettingsResponse> listLoyaltySettings(Long companyId);
}
