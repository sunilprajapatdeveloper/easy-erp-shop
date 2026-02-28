package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.response.LoyaltySettingsResponse;
import nextpos.app.nextpos.service.interf.LoyaltySettingsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loyalty-settings")
@RequiredArgsConstructor
public class LoyaltySettingsController {

    private final LoyaltySettingsService loyaltySettingsService;

    /**
     * Create a new LoyaltySettings for a company
     */
    @PostMapping
    public ResponseEntity<LoyaltySettingsResponse> createLoyaltySettings(
            @RequestBody @Valid CreateLoyaltySettingsRequest request) {
        LoyaltySettingsResponse response = loyaltySettingsService.createLoyaltySettings(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing LoyaltySettings
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoyaltySettingsResponse> updateLoyaltySettings(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLoyaltySettingsRequest request) {
        LoyaltySettingsResponse response = loyaltySettingsService.updateLoyaltySettings(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific LoyaltySettings by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoyaltySettingsResponse> getLoyaltySettings(
            @PathVariable Long id) {
        LoyaltySettingsResponse response = loyaltySettingsService.getLoyaltySettings(id);
        return ResponseEntity.ok(response);
    }

    /**
     * List all LoyaltySettings for a company
     */
    @GetMapping
    public ResponseEntity<List<LoyaltySettingsResponse>> listLoyaltySettings() {
        List<LoyaltySettingsResponse> responseList = loyaltySettingsService.listLoyaltySettings();
        return ResponseEntity.ok(responseList);
    }
}