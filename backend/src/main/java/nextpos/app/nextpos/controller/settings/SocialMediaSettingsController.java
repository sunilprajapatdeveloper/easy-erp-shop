package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SocialMediaSettingsResponse;
import nextpos.app.nextpos.service.interf.SocialMediaSettingsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social-media-settings")
@RequiredArgsConstructor
public class SocialMediaSettingsController {

    private final SocialMediaSettingsService service;

    /**
     * Create a new social media settings for a company
     */
    @PostMapping
    public ResponseEntity<SocialMediaSettingsResponse> create(
            @Valid @RequestBody CreateSocialMediaSettingsRequest request,
            @RequestParam Long createdBy) {
        SocialMediaSettingsResponse response = service.createSocialMediaSettings(request, createdBy);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing social media settings for a company
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialMediaSettingsResponse> update(
            @PathVariable Long id,
            @RequestParam Long companyId,
            @Valid @RequestBody UpdateSocialMediaSettingsRequest request,
            @RequestParam Long updatedBy) {
        SocialMediaSettingsResponse response = service.updateSocialMediaSettings(id, companyId, request, updatedBy);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific social media settings by id and company
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialMediaSettingsResponse> get(
            @PathVariable Long id,
            @RequestParam Long companyId) {
        SocialMediaSettingsResponse response = service.getSocialMediaSettings(id, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all social media settings for a company
     */
    @GetMapping
    public ResponseEntity<List<SocialMediaSettingsResponse>> list(
            @RequestParam Long companyId) {
        List<SocialMediaSettingsResponse> responseList = service.listSocialMediaSettings(companyId);
        return ResponseEntity.ok(responseList);
    }
}