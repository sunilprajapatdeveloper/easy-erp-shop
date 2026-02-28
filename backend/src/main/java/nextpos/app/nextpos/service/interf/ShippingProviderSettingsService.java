package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;

import java.util.List;

public interface ShippingProviderSettingsService {

    ShippingProviderSettingsResponse createShippingProviderSettings(CreateShippingProviderSettingsRequest request);

    ShippingProviderSettingsResponse updateShippingProviderSettings(Long id, Long warehouseId,
            UpdateShippingProviderSettingsRequest request);

    ShippingProviderSettingsResponse getShippingProviderSettings(Long id, Long warehouseId);

    List<ShippingProviderSettingsResponse> listShippingProviderSettingsByCompany();

    List<ShippingProviderSettingsResponse> listShippingProviderSettingsByWarehouse(Long warehouseId);
}