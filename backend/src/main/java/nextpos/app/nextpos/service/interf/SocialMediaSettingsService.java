package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SocialMediaSettingsResponse;

import java.util.List;

public interface SocialMediaSettingsService {

    SocialMediaSettingsResponse createSocialMediaSettings(CreateSocialMediaSettingsRequest request);

    SocialMediaSettingsResponse updateSocialMediaSettings(Long id, UpdateSocialMediaSettingsRequest request);

    SocialMediaSettingsResponse getSocialMediaSettings(Long id);

    List<SocialMediaSettingsResponse> listSocialMediaSettings();
}