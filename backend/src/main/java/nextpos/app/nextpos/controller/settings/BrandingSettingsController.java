package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.BrandingSettingsResponse;
import nextpos.app.nextpos.service.interf.BrandingSettingsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/branding-settings")
@RequiredArgsConstructor
public class BrandingSettingsController {

    private final BrandingSettingsService brandingSettingsService;

    /**
     * Create branding settings for a company.
     */
    @PostMapping
    public ResponseEntity<BrandingSettingsResponse> createBrandingSettings(
            @Valid @RequestBody CreateBrandingSettingsRequest request) {

        log.info("CreateBrandingSettings request");

        BrandingSettingsResponse response = brandingSettingsService.createBrandingSettings(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get branding settings by id and company.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandingSettingsResponse> getBrandingSettings(
            @PathVariable("id") Long id) {

        log.info("GetBrandingSettings id={}", id);
        BrandingSettingsResponse response = brandingSettingsService.getBrandingSettings(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update branding settings.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandingSettingsResponse> updateBrandingSettings(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateBrandingSettingsRequest request) {

        log.info("UpdateBrandingSettings id={}", id);
        BrandingSettingsResponse response = brandingSettingsService.updateBrandingSettings(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete branding settings (scoped by id + company).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandingSettings(
            @PathVariable("id") Long id) {

        log.info("DeleteBrandingSettings id={}", id);
        brandingSettingsService.deleteBrandingSettings(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all branding settings for a company
     */
    @GetMapping
    public ResponseEntity<List<BrandingSettingsResponse>> listBrandingSettings() {

        log.info("ListBrandingSettings");
        List<BrandingSettingsResponse> list = brandingSettingsService.listBrandingSettings();
        return ResponseEntity.ok(list);
    }
}
