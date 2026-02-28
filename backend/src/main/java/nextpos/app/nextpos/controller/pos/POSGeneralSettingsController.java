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
@RequiredArgsConstructor
public class POSGeneralSettingsController {

    private final POSGeneralSettingsService posGeneralSettingsService;

    /**
     * Create POS general settings for a warehouse
     */
    @PostMapping
    public ResponseEntity<POSGeneralSettingsResponse> createPOSSettings(
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @Valid @RequestBody CreatePOSGeneralSettingsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(posGeneralSettingsService.createPOSSettings(warehouseId, request));
    }

    /**
     * Get POS settings by warehouse
     */
    @GetMapping
    public ResponseEntity<POSGeneralSettingsResponse> getPOSSettingsByWarehouse(
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {
        return ResponseEntity.ok(posGeneralSettingsService.getByWarehouse(warehouseId));
    }

    /**
     * Update POS settings
     */
    @PutMapping("/{id}")
    public ResponseEntity<POSGeneralSettingsResponse> updatePOSSettings(
            @RequestHeader("X-Warehouse-Id") Long warehouseId, @PathVariable Long id,
            @Valid @RequestBody UpdatePOSGeneralSettingsRequest request) {
        return ResponseEntity
                .ok(posGeneralSettingsService.updatePOSSettings(warehouseId, id, request));
    }

    /**
     * Delete POS settings (soft delete if needed)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOSSettings(@RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id) {
        posGeneralSettingsService.deletePOSSettings(warehouseId, id);
        return ResponseEntity.noContent().build();
    }
}
