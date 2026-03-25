package nextpos.app.nextpos.ai.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogger {
    public void logRequest(String tenant, String userId, String promptKey, String model, int tokens, long latency,
            boolean cached) {
        // Structured logging (e.g., JSON)
        log.info("AI_REQUEST: tenant={}, user={}, promptKey={}, model={}, tokens={}, latency={}, cached={}",
                tenant, userId, promptKey, model, tokens, latency, cached);
    }

    public void logError(String tenant, String userId, String promptKey, String error) {
        log.error("AI_ERROR: tenant={}, user={}, promptKey={}, error={}", tenant, userId, promptKey, error);
    }
}