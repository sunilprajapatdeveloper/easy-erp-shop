package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.OnlineOrderingSettingsResponse;
import nextpos.app.nextpos.service.interf.OnlineOrderingSettingsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/online-ordering-settings")
@RequiredArgsConstructor
public class OnlineOrderingSettingsController {

    private final OnlineOrderingSettingsService onlineOrderingSettingsService;

    @PostMapping
    public ResponseEntity<OnlineOrderingSettingsResponse> createSettings(
            @Valid @RequestBody CreateOnlineOrderingSettingsRequest request,
            @RequestHeader("X-User-Id") Long createdBy) {
        log.info("Creating OnlineOrderingSettings by userId={}", createdBy);
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.createOnlineOrderingSettings(request,
                createdBy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<OnlineOrderingSettingsResponse> updateSettings(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateOnlineOrderingSettingsRequest request,
            @RequestHeader("X-User-Id") Long updatedBy) {
        log.info("Updating OnlineOrderingSettings for companyId={} by userId={}", companyId, updatedBy);
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.updateOnlineOrderingSettings(request,
                updatedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<OnlineOrderingSettingsResponse> getSettings(@PathVariable Long companyId) {
        log.info("Fetching OnlineOrderingSettings for companyId={}", companyId);
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.getOnlineOrderingSettings(companyId);
        return ResponseEntity.ok(response);
    }
}
