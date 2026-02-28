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
    @Operation(summary = "Create or update SMTP settings for the current company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SMTP settings created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<SMTPSettingsResponse> createOrUpdateSMTPSettings(
            @Valid @RequestBody CreateSMTPSettingsRequest request) {
        SMTPSettingsResponse response = smtpSettingsService.createOrUpdateSMTPSettings(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(summary = "Update existing SMTP settings for the current company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMTP settings updated successfully"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    public ResponseEntity<SMTPSettingsResponse> updateSMTPSettings(
            @Valid @RequestBody UpdateSMTPSettingsRequest request) {
        SMTPSettingsResponse response = smtpSettingsService.updateSMTPSettings(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get SMTP settings for the current company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMTP settings found"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    public ResponseEntity<SMTPSettingsResponse> getSMTPSettings() {
        SMTPSettingsResponse response = smtpSettingsService.getSMTPSettings();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Delete (deactivate) SMTP settings for the current company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SMTP settings deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "SMTP settings not found")
    })
    public ResponseEntity<Void> deleteSMTPSettings() {
        smtpSettingsService.deleteSMTPSettings();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test")
    @Operation(summary = "Test SMTP connection for the current company")
    public ResponseEntity<Map<String, Object>> testConnection() {
        boolean isConnected = smtpSettingsService.testConnection();
        return ResponseEntity.ok(Map.of(
                "connectionStatus", isConnected ? "SUCCESS" : "FAILED",
                "timestamp", LocalDateTime.now()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh SMTP cache for the current company (after settings change)")
    public ResponseEntity<Map<String, Object>> refreshCache() {
        smtpSettingsService.refreshCache();
        return ResponseEntity.ok(Map.of(
                "message", "SMTP cache refreshed successfully",
                "timestamp", LocalDateTime.now()));
    }
}