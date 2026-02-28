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
            @Valid @RequestBody CreateOnlineOrderingSettingsRequest request) {
        log.info("Creating OnlineOrderingSettings");
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.createOnlineOrderingSettings(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OnlineOrderingSettingsResponse> updateSettings(
            @Valid @RequestBody UpdateOnlineOrderingSettingsRequest request) {
        log.info("Updating OnlineOrderingSettings");
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.updateOnlineOrderingSettings(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<OnlineOrderingSettingsResponse> getSettings() {
        log.info("Fetching OnlineOrderingSettings");
        OnlineOrderingSettingsResponse response = onlineOrderingSettingsService.getOnlineOrderingSettings();
        return ResponseEntity.ok(response);
    }
}
