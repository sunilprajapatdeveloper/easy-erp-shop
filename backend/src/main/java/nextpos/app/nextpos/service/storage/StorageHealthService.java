package nextpos.app.nextpos.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageHealthService {

    private final StorageServiceFactory storageServiceFactory;

    public Map<String, Object> checkHealth() {
        Map<String, Object> healthInfo = new HashMap<>();

        try {
            StorageService activeService = storageServiceFactory.getStorageService();
            String provider = activeService.getProviderName();

            // Test storage connectivity
            boolean isHealthy = testStorage(activeService);

            healthInfo.put("provider", provider);
            healthInfo.put("status", isHealthy ? "UP" : "DOWN");
            healthInfo.put("timestamp", Instant.now().toString());

            if (!isHealthy) {
                healthInfo.put("error", "Storage test failed");
            }

        } catch (Exception e) {
            log.error("Storage health check failed", e);
            healthInfo.put("status", "DOWN");
            healthInfo.put("error", e.getMessage());
        }

        return healthInfo;
    }

    private boolean testStorage(StorageService storageService) {
        try {
            // Simple test - just check if the service is available
            return storageService != null && storageService.getProviderName() != null;
        } catch (Exception e) {
            log.error("Storage test failed", e);
            return false;
        }
    }
}