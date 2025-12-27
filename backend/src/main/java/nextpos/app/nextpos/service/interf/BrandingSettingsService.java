package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.BrandingSettingsResponse;

import java.util.List;

public interface BrandingSettingsService {

    BrandingSettingsResponse createBrandingSettings(CreateBrandingSettingsRequest request, Long companyId,
            Long createdBy);

    BrandingSettingsResponse updateBrandingSettings(Long id, Long companyId, UpdateBrandingSettingsRequest request,
            Long updatedBy);

    BrandingSettingsResponse getBrandingSettings(Long id, Long companyId);

    List<BrandingSettingsResponse> listBrandingSettings(Long companyId);

    void deleteBrandingSettings(Long id, Long companyId);
}