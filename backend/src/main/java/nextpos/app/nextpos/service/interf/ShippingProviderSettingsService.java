package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;

import java.util.List;

public interface ShippingProviderSettingsService {

    ShippingProviderSettingsResponse createShippingProviderSettings(CreateShippingProviderSettingsRequest request,
            Long createdBy);

    ShippingProviderSettingsResponse updateShippingProviderSettings(Long id, Long companyId, Long warehouseId,
            UpdateShippingProviderSettingsRequest request, Long updatedBy);

    ShippingProviderSettingsResponse getShippingProviderSettings(Long id, Long companyId, Long warehouseId);

    List<ShippingProviderSettingsResponse> listShippingProviderSettingsByCompany(Long companyId);

    List<ShippingProviderSettingsResponse> listShippingProviderSettingsByWarehouse(Long companyId, Long warehouseId);
}