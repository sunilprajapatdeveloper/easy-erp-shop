package nextpos.app.nextpos.controller.pos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.response.POSGeneralSettingsResponse;
import nextpos.app.nextpos.service.interf.POSGeneralSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pos/settings")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class POSGeneralSettingsController {

    private final POSGeneralSettingsService posGeneralSettingsService;

    /**
     * Create POS general settings for a warehouse
     */
    @PostMapping
    public ResponseEntity<POSGeneralSettingsResponse> createPOSSettings(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreatePOSGeneralSettingsRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(posGeneralSettingsService.createPOSSettings(companyId, warehouseId, createdBy, request));
    }

    /**
     * Get POS settings by warehouse
     */
    @GetMapping
    public ResponseEntity<POSGeneralSettingsResponse> getPOSSettingsByWarehouse(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {

        return ResponseEntity.ok(posGeneralSettingsService.getByWarehouse(companyId, warehouseId));
    }

    /**
     * Update POS settings
     */
    @PutMapping("/{id}")
    public ResponseEntity<POSGeneralSettingsResponse> updatePOSSettings(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePOSGeneralSettingsRequest request) {

        return ResponseEntity
                .ok(posGeneralSettingsService.updatePOSSettings(companyId, warehouseId, updatedBy, id, request));
    }

    /**
     * Delete POS settings (soft delete if needed)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOSSettings(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @RequestHeader("X-User-Id") Long deletedBy,
            @PathVariable Long id) {

        posGeneralSettingsService.deletePOSSettings(companyId, warehouseId, deletedBy, id);
        return ResponseEntity.noContent().build();
    }
}
