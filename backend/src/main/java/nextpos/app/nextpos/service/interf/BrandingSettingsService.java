package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.BrandingSettingsResponse;

import java.util.List;

public interface BrandingSettingsService {

        BrandingSettingsResponse createBrandingSettings(CreateBrandingSettingsRequest request);

        BrandingSettingsResponse updateBrandingSettings(Long id, UpdateBrandingSettingsRequest request);

        BrandingSettingsResponse getBrandingSettings(Long id);

        List<BrandingSettingsResponse> listBrandingSettings();

        void deleteBrandingSettings(Long id);
}