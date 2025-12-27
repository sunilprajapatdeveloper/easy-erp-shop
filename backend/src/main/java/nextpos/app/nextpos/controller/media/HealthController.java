package nextpos.app.nextpos.controller.media;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.service.storage.StorageHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private final StorageHealthService storageHealthService;

    @GetMapping("/storage")
    public ResponseEntity<Map<String, Object>> getStorageHealth() {
        Map<String, Object> healthInfo = storageHealthService.checkHealth();
        return ResponseEntity.ok(healthInfo);
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "NextPOS Media Service"));
    }
}