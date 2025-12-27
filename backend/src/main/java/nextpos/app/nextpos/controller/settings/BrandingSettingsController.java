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
     *
     * Headers required:
     * X-Company-Id : tenant company id (Long)
     * X-User-Id : authenticated user id (Long) - used as createdBy
     */
    @PostMapping
    public ResponseEntity<BrandingSettingsResponse> createBrandingSettings(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreateBrandingSettingsRequest request) {

        log.info("CreateBrandingSettings request for companyId={} by userId={}", companyId, createdBy);

        BrandingSettingsResponse response = brandingSettingsService.createBrandingSettings(request, companyId,
                createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get branding settings by id and company.
     *
     * Header required:
     * X-Company-Id : tenant company id (Long)
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandingSettingsResponse> getBrandingSettings(
            @PathVariable("id") Long id,
            @RequestHeader("X-Company-Id") Long companyId) {

        log.info("GetBrandingSettings id={} for companyId={}", id, companyId);
        BrandingSettingsResponse response = brandingSettingsService.getBrandingSettings(id, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update branding settings.
     *
     * Headers required:
     * X-Company-Id : tenant company id (Long)
     * X-User-Id : authenticated user id (Long) - used as updatedBy
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandingSettingsResponse> updateBrandingSettings(
            @PathVariable("id") Long id,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @Valid @RequestBody UpdateBrandingSettingsRequest request) {

        log.info("UpdateBrandingSettings id={} for companyId={} by userId={}", id, companyId, updatedBy);
        BrandingSettingsResponse response = brandingSettingsService.updateBrandingSettings(id, companyId, request,
                updatedBy);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete branding settings (scoped by id + company).
     *
     * Headers required:
     * X-Company-Id : tenant company id (Long)
     * X-User-Id : authenticated user id (Long) - for audit if needed by service
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandingSettings(
            @PathVariable("id") Long id,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader(value = "X-User-Id", required = false) Long deletedBy) {

        log.info("DeleteBrandingSettings id={} for companyId={} by userId={}", id, companyId, deletedBy);
        brandingSettingsService.deleteBrandingSettings(id, companyId);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all branding settings for a company
     *
     * Header required:
     * X-Company-Id : tenant company id (Long)
     */
    @GetMapping
    public ResponseEntity<List<BrandingSettingsResponse>> listBrandingSettings(
            @RequestHeader("X-Company-Id") Long companyId) {

        log.info("ListBrandingSettings for companyId={}", companyId);
        List<BrandingSettingsResponse> list = brandingSettingsService.listBrandingSettings(companyId);
        return ResponseEntity.ok(list);
    }
}
