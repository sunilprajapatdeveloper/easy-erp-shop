package nextpos.app.nextpos.controller.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;
import nextpos.app.nextpos.service.interf.ShippingProviderSettingsService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/settings/shipping-provider")
@RequiredArgsConstructor
public class ShippingProviderSettingsController {

    private final ShippingProviderSettingsService shippingProviderSettingsService;

    /**
     * Create new shipping provider settings for a company and warehouse.
     */
    @PostMapping
    public ResponseEntity<ShippingProviderSettingsResponse> create(
            @Validated @RequestBody CreateShippingProviderSettingsRequest request) {
        log.info("Creating shipping provider settings for warehouseId={}", request.getWarehouseId());
        ShippingProviderSettingsResponse response = shippingProviderSettingsService
                .createShippingProviderSettings(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update existing shipping provider settings.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShippingProviderSettingsResponse> update(
            @PathVariable Long id,
            @RequestParam Long warehouseId,
            @Validated @RequestBody UpdateShippingProviderSettingsRequest request) {
        log.info("Updating shipping provider settings id={} for warehouseId={}", id, warehouseId);
        ShippingProviderSettingsResponse response = shippingProviderSettingsService.updateShippingProviderSettings(id,
                warehouseId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a shipping provider setting by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShippingProviderSettingsResponse> get(
            @PathVariable Long id,
            @RequestParam Long warehouseId) {
        ShippingProviderSettingsResponse response = shippingProviderSettingsService.getShippingProviderSettings(id,
                warehouseId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all shipping provider settings for a company.
     */
    @GetMapping("/company")
    public ResponseEntity<List<ShippingProviderSettingsResponse>> listByCompany() {
        List<ShippingProviderSettingsResponse> responseList = shippingProviderSettingsService
                .listShippingProviderSettingsByCompany();
        return ResponseEntity.ok(responseList);
    }

    /**
     * List all shipping provider settings for a warehouse.
     */
    @GetMapping("/warehouse")
    public ResponseEntity<List<ShippingProviderSettingsResponse>> listByWarehouse(
            @RequestParam Long warehouseId) {
        List<ShippingProviderSettingsResponse> responseList = shippingProviderSettingsService
                .listShippingProviderSettingsByWarehouse(warehouseId);
        return ResponseEntity.ok(responseList);
    }
}