package nextpos.app.nextpos.ai.cache;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CacheKeyGenerator {
    public String generateKey(String tenantId, String promptKey, String renderedPrompt, Map<String, Object> context,
            String toolsHash) {
        // Use a deterministic hash of the prompt and context
        // For simplicity, use a string concatenation and hash
        String keyBase = String.format("%s:%s:%s:%s:%s", tenantId, promptKey, renderedPrompt, context, toolsHash);
        // In production, use a proper hash (e.g., SHA-256) to avoid key length issues
        return "ai:" + Integer.toHexString(keyBase.hashCode());
    }
}