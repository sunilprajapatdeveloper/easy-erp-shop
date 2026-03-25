package nextpos.app.nextpos.ai.cost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.AiUsageLog;
import nextpos.app.nextpos.repository.AiUsageLogRepository;
import nextpos.app.nextpos.repository.TenantAiSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CostTracker {
    private final TenantAiSettingsRepository settingsRepository;
    private final AiUsageLogRepository usageLogRepository;

    @Transactional
    public void trackUsage(String tenantId, String userId, UUID requestId, String promptKey,
            String model, int inputTokens, int outputTokens, long latencyMs, boolean cached,
            String status, String errorMessage) {
        int totalTokens = inputTokens + outputTokens;
        double costValue = calculateCost(model, totalTokens);

        // Update tenant token usage
        if (!cached && status.equals("success")) {
            int updated = settingsRepository.incrementTokens(tenantId, totalTokens);
            if (updated == 0) {
                log.warn("Failed to increment tokens for tenant {} (quota exceeded or race condition)", tenantId);
                // Could throw exception, but we'll just log and continue; already enforced at
                // entry
            }
        }

        // Save usage log
        AiUsageLog usageLog = new AiUsageLog();
        usageLog.setTenantId(tenantId);
        usageLog.setUserId(userId);
        usageLog.setRequestId(requestId);
        usageLog.setPromptKey(promptKey);
        usageLog.setModel(model);
        usageLog.setInputTokens(inputTokens);
        usageLog.setOutputTokens(outputTokens);
        usageLog.setTotalTokens(totalTokens);
        usageLog.setCost(BigDecimal.valueOf(costValue));
        usageLog.setLatencyMs((int) latencyMs);
        usageLog.setCached(cached);
        usageLog.setStatus(status);
        usageLog.setErrorMessage(errorMessage);
        usageLogRepository.save(usageLog);
    }

    private double calculateCost(String model, int tokens) {
        // Implement based on model pricing (e.g., store rates in DB or config)
        // For simplicity, use a config map
        // For production, store in a separate table ai_model_rates
        return tokens * 0.000001; // placeholder
    }
}