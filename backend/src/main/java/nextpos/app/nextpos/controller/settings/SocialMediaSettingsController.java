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
            @Valid @RequestBody CreateSocialMediaSettingsRequest request) {
        SocialMediaSettingsResponse response = service.createSocialMediaSettings(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing social media settings for a company
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialMediaSettingsResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSocialMediaSettingsRequest request) {
        SocialMediaSettingsResponse response = service.updateSocialMediaSettings(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific social media settings by id and company
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialMediaSettingsResponse> get(
            @PathVariable Long id) {
        SocialMediaSettingsResponse response = service.getSocialMediaSettings(id);
        return ResponseEntity.ok(response);
    }

    /**
     * List all social media settings for a company
     */
    @GetMapping
    public ResponseEntity<List<SocialMediaSettingsResponse>> list() {
        List<SocialMediaSettingsResponse> responseList = service.listSocialMediaSettings();
        return ResponseEntity.ok(responseList);
    }
}