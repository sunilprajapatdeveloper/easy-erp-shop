package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.response.SecuritySettingsResponse;
import nextpos.app.nextpos.service.interf.SecuritySettingsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings/security")
@RequiredArgsConstructor
public class SecuritySettingsController {

    private final SecuritySettingsService securitySettingsService;

    /**
     * Create new SecuritySettings for a company
     */
    @PostMapping("/company/{companyId}")
    public ResponseEntity<SecuritySettingsResponse> createSecuritySettings(
            @PathVariable Long companyId,
            @Valid @RequestBody CreateSecuritySettingsRequest request) {
        SecuritySettingsResponse response = securitySettingsService.createSecuritySettings(companyId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existing SecuritySettings for a company
     */
    @PutMapping("/company/{companyId}")
    public ResponseEntity<SecuritySettingsResponse> updateSecuritySettings(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateSecuritySettingsRequest request) {
        SecuritySettingsResponse response = securitySettingsService.updateSecuritySettings(companyId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get SecuritySettings for a company
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<SecuritySettingsResponse> getSecuritySettings(@PathVariable Long companyId) {
        SecuritySettingsResponse response = securitySettingsService.getSecuritySettings(companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all SecuritySettings (admin only)
     */
    @GetMapping("/all")
    public ResponseEntity<List<SecuritySettingsResponse>> listAllSecuritySettings() {
        List<SecuritySettingsResponse> responseList = securitySettingsService.listAllSecuritySettings();
        return ResponseEntity.ok(responseList);
    }
}