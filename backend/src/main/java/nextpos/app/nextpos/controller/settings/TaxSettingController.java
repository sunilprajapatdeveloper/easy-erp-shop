package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;
import nextpos.app.nextpos.service.interf.TaxSettingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Tax Settings.
 * 
 * Supports multi-tenancy via company scoping.
 * Provides CRUD endpoints for configuring taxes
 * like GST, VAT, Sales Tax, etc.
 */
@RestController
@RequestMapping("/api/tax-settings")
@RequiredArgsConstructor
public class TaxSettingController {

    private final TaxSettingService taxSettingService;

    /**
     * Create a new tax setting for a company.
     */
    @PostMapping
    public ResponseEntity<TaxSettingResponse> createTaxSetting(
            @Valid @RequestBody CreateTaxSettingRequest request) {
        TaxSettingResponse response = taxSettingService.createTaxSetting(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single tax setting by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxSettingResponse> getTaxSetting(
            @PathVariable Long id) {
        TaxSettingResponse response = taxSettingService.getTaxSetting(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get active tax setting for warehouse.
     */
    @GetMapping("/active")
    public ResponseEntity<TaxSettingResponse> getActiveTaxSetting(
            @RequestParam(required = false) Long warehouseId) {

        TaxSettingResponse response = taxSettingService.getActiveTaxSetting(warehouseId);

        return ResponseEntity.ok(response);
    }

    /**
     * List all tax settings for a company.
     */
    @GetMapping
    public ResponseEntity<List<TaxSettingResponse>> listTaxSettings() {
        List<TaxSettingResponse> responses = taxSettingService.listTaxSettings();
        return ResponseEntity.ok(responses);
    }

    /**
     * Update an existing tax setting.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxSettingResponse> updateTaxSetting(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaxSettingRequest request) {
        TaxSettingResponse response = taxSettingService.updateTaxSetting(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a tax setting by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxSetting(
            @PathVariable Long id) {
        taxSettingService.deleteTaxSetting(id);
        return ResponseEntity.noContent().build();
    }
}
