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
            @RequestHeader("X-Company-Id") Long companyId,
            @Valid @RequestBody CreateTaxSettingRequest request) {
        TaxSettingResponse response = taxSettingService.createTaxSetting(request, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single tax setting by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxSettingResponse> getTaxSetting(
            @PathVariable Long id,
            @RequestHeader("X-Company-Id") Long companyId) {
        TaxSettingResponse response = taxSettingService.getTaxSetting(id, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all tax settings for a company.
     */
    @GetMapping
    public ResponseEntity<List<TaxSettingResponse>> listTaxSettings(
            @RequestHeader("X-Company-Id") Long companyId) {
        List<TaxSettingResponse> responses = taxSettingService.listTaxSettings(companyId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update an existing tax setting.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxSettingResponse> updateTaxSetting(
            @PathVariable Long id,
            @RequestHeader("X-Company-Id") Long companyId,
            @Valid @RequestBody UpdateTaxSettingRequest request) {
        TaxSettingResponse response = taxSettingService.updateTaxSetting(id, companyId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a tax setting by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxSetting(
            @PathVariable Long id,
            @RequestHeader("X-Company-Id") Long companyId) {
        taxSettingService.deleteTaxSetting(id, companyId);
        return ResponseEntity.noContent().build();
    }
}
