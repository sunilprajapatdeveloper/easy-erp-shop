package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.response.POSGeneralSettingsResponse;

public interface POSGeneralSettingsService {

    /**
     * Create POS settings for a warehouse. companyId & warehouseId come from
     * headers.
     */
    POSGeneralSettingsResponse createPOSSettings(Long companyId, Long warehouseId, Long createdBy,
            CreatePOSGeneralSettingsRequest request);

    /**
     * Get POS settings by company + warehouse (headers)
     */
    POSGeneralSettingsResponse getByWarehouse(Long companyId, Long warehouseId);

    /**
     * Update POS settings (scoped by company + warehouse and id)
     */
    POSGeneralSettingsResponse updatePOSSettings(Long companyId, Long warehouseId, Long updatedBy,
            Long id, UpdatePOSGeneralSettingsRequest request);

    /**
     * Delete POS settings (scoped). This does permanent delete; change to soft
     * delete if desired.
     */
    void deletePOSSettings(Long companyId, Long warehouseId, Long deletedBy, Long id);
}
