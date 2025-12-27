package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SocialMediaSettingsResponse;

import java.util.List;

public interface SocialMediaSettingsService {

    SocialMediaSettingsResponse createSocialMediaSettings(CreateSocialMediaSettingsRequest request, Long createdBy);

    SocialMediaSettingsResponse updateSocialMediaSettings(Long id, Long companyId,
            UpdateSocialMediaSettingsRequest request, Long updatedBy);

    SocialMediaSettingsResponse getSocialMediaSettings(Long id, Long companyId);

    List<SocialMediaSettingsResponse> listSocialMediaSettings(Long companyId);
}