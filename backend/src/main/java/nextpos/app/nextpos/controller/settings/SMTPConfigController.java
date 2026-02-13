package nextpos.app.nextpos.controller.settings;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SMTPSettingsResponse;
import nextpos.app.nextpos.service.interf.SMTPSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/smtp")
@RequiredArgsConstructor
@Tag(name = "SMTP Configuration", description = "Manage company SMTP email settings")
public class SMTPConfigController {

    private final SMTPSettingsService smtpSettingsService;

    @PostMapping
    @Operation(summary = "Create or update SMTP settings for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SMTP settings created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#request.companyId)")
    public ResponseEntity<SMTPSettingsResponse> createOrUpdateSMTPSettings(
            @Valid @RequestBody CreateSMTPSettingsRequest request) {
        SMTPSettingsResponse response = smtpSettingsService.createOrUpdateSMTPSettings(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Update existing SMTP settings for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMTP settings updated successfully"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#companyId)")
    public ResponseEntity<SMTPSettingsResponse> updateSMTPSettings(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateSMTPSettingsRequest request) {
        SMTPSettingsResponse response = smtpSettingsService.updateSMTPSettings(companyId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Get SMTP settings by company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMTP settings found"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#companyId)")
    public ResponseEntity<SMTPSettingsResponse> getSMTPSettings(@PathVariable Long companyId) {
        SMTPSettingsResponse response = smtpSettingsService.getSMTPSettingsByCompanyId(companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Delete (deactivate) SMTP settings for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SMTP settings deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#companyId)")
    public ResponseEntity<Void> deleteSMTPSettings(@PathVariable Long companyId) {
        smtpSettingsService.deleteSMTPSettings(companyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test/{companyId}")
    @Operation(summary = "Test SMTP connection for a company")
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#companyId)")
    public ResponseEntity<Map<String, Object>> testConnection(@PathVariable Long companyId) {
        boolean isConnected = smtpSettingsService.testConnection(companyId);
        return ResponseEntity.ok(Map.of(
                "companyId", companyId,
                "connectionStatus", isConnected ? "SUCCESS" : "FAILED",
                "timestamp", LocalDateTime.now()));
    }

    @PostMapping("/refresh/{companyId}")
    @Operation(summary = "Refresh SMTP cache for a company (after settings change)")
    @PreAuthorize("@permissionEvaluator.hasCompanyAccess(#companyId)")
    public ResponseEntity<Map<String, Object>> refreshCache(@PathVariable Long companyId) {
        smtpSettingsService.refreshCache(companyId);
        return ResponseEntity.ok(Map.of(
                "message", "SMTP cache refreshed successfully",
                "companyId", companyId,
                "timestamp", LocalDateTime.now()));
    }
}