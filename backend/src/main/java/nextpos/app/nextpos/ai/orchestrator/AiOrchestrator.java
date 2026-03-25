package nextpos.app.nextpos.ai.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.cache.AiCacheService;
import nextpos.app.nextpos.ai.cache.CacheKeyGenerator;
import nextpos.app.nextpos.ai.context.ContextBuilder;
import nextpos.app.nextpos.ai.context.TenantContext;
import nextpos.app.nextpos.ai.cost.CostTracker;
import nextpos.app.nextpos.ai.cost.QuotaEnforcer;
import nextpos.app.nextpos.ai.dto.AiRequest;
import nextpos.app.nextpos.ai.dto.AiResponse;
import nextpos.app.nextpos.ai.exception.AiException;
import nextpos.app.nextpos.ai.exception.QuotaExceededException;
import nextpos.app.nextpos.ai.monitoring.AiMetrics;
import nextpos.app.nextpos.ai.monitoring.AuditLogger;
import nextpos.app.nextpos.ai.prompt.PromptService;
import nextpos.app.nextpos.ai.provider.AiProvider;
import nextpos.app.nextpos.ai.provider.ProviderFactory;
import nextpos.app.nextpos.ai.tools.ToolExecutor;
import nextpos.app.nextpos.ai.tools.ToolRegistry;
import nextpos.app.nextpos.model.entity.AiPrompt;
import nextpos.app.nextpos.model.entity.TenantAiSettings;
import nextpos.app.nextpos.repository.TenantAiSettingsRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiOrchestrator {
    private final TenantAiSettingsRepository settingsRepository;
    private final PromptService promptService;
    private final ContextBuilder contextBuilder;
    private final ToolRegistry toolRegistry;
    private final ToolExecutor toolExecutor;
    private final ProviderFactory providerFactory;
    private final AiCacheService cacheService;
    private final CacheKeyGenerator cacheKeyGenerator;
    private final CostTracker costTracker;
    private final QuotaEnforcer quotaEnforcer;
    private final AiMetrics metrics;
    private final AuditLogger auditLogger;
    private final ObjectMapper objectMapper;

    public AiResponse<?> orchestrate(String tenantId, String userId, AiRequest request) {
        UUID requestId = UUID.randomUUID();
        long startTimeMillis = System.currentTimeMillis();
        Timer.Sample timer = metrics.startTimer();

        try {
            TenantAiSettings settings = settingsRepository.findByTenantId(tenantId)
                    .orElseThrow(() -> new AiException("Tenant AI settings not found"));

            String promptKey = request.getPromptKey() != null ? request.getPromptKey()
                    : getDefaultPromptKeyForRequest(request);
            if (!settings.getEnabledFeatures().contains(promptKey)) {
                throw new AiException("AI feature not enabled for tenant: " + promptKey);
            }

            TenantContext tenantCtx = new TenantContext();
            tenantCtx.setTenantId(tenantId);
            tenantCtx.setUserId(userId);
            Map<String, Object> context = contextBuilder.buildContext(tenantCtx, request.getContext());

            AiPrompt promptEntity = promptService.getActivePrompt(promptKey);
            Map<String, Object> allVariables = new HashMap<>();
            allVariables.putAll(request.getVariables() != null ? request.getVariables() : Map.of());
            allVariables.put("context", context);
            allVariables.put("query", request.getQuery());
            String renderedPrompt = promptService.getRenderedPrompt(promptKey, allVariables);

            List<AiProvider.Message> messages = new ArrayList<>(List.of(
                    new AiProvider.Message("system", "You are an AI assistant for ERP system."),
                    new AiProvider.Message("user", renderedPrompt)));

            List<AiProvider.ToolDefinition> tools = toolRegistry.getProviderToolDefinitions();

            if (!request.getStream()) {
                String cacheKey = cacheKeyGenerator.generateKey(tenantId, promptKey, renderedPrompt, context,
                        hashTools(tools));
                Optional<AiResponse<?>> cached = cacheService.get(cacheKey);
                if (cached.isPresent()) {
                    metrics.recordCacheHit(tenantId);
                    long latency = System.currentTimeMillis() - startTimeMillis;
                    costTracker.trackUsage(tenantId, userId, requestId, promptKey, settings.getModelName(),
                            0, 0, latency, true, "success", null);
                    auditLogger.logRequest(tenantId, userId, promptKey, settings.getModelName(), 0, latency, true);

                    // Fixed: getModelProvider() -> getAiProvider()
                    metrics.stopTimer(timer, tenantId, settings.getAiProvider());
                    return cached.get();
                }
                metrics.recordCacheMiss(tenantId);
            }

            int estimatedTokens = renderedPrompt.length() / 4;
            quotaEnforcer.checkQuota(tenantId, estimatedTokens);

            AiProvider provider = providerFactory.getProvider(settings);
            AiProvider.AiRequestOptions options = new AiProvider.AiRequestOptions(
                    settings.getModelName(),
                    promptEntity.getTemperature().doubleValue(),
                    promptEntity.getMaxTokens(),
                    settings.getRequestTimeoutMs());

            AiResponse<String> providerResponse;
            providerResponse = provider.complete(messages, tools, options);

            if (providerResponse.getToolCalls() != null && !providerResponse.getToolCalls().isEmpty()) {
                for (AiResponse.ToolCall toolCall : providerResponse.getToolCalls()) {
                    Object toolResult = toolExecutor.execute(requestId, toolCall);
                    messages.add(new AiProvider.Message("assistant", null));
                    messages.add(new AiProvider.Message("tool", objectMapper.writeValueAsString(toolResult)));
                }
                providerResponse = provider.complete(messages, tools, options);
            }

            AiResponse<Object> finalResponse = AiResponse.builder()
                    .result(providerResponse.getResult())
                    .tokensUsed(providerResponse.getTokensUsed())
                    .model(settings.getModelName())
                    .latencyMs(System.currentTimeMillis() - startTimeMillis)
                    .cached(false)
                    .toolCalls(providerResponse.getToolCalls())
                    .build();

            if (!request.getStream()) {
                String cacheKey = cacheKeyGenerator.generateKey(tenantId, promptKey, renderedPrompt, context,
                        hashTools(tools));
                cacheService.put(cacheKey, finalResponse, getCacheTtl(promptKey));
            }

            long finalLatency = System.currentTimeMillis() - startTimeMillis;
            int tokens = providerResponse.getTokensUsed() != null ? providerResponse.getTokensUsed() : 0;

            costTracker.trackUsage(tenantId, userId, requestId, promptKey, settings.getModelName(),
                    tokens / 2, tokens / 2, finalLatency, false, "success", null);
            auditLogger.logRequest(tenantId, userId, promptKey, settings.getModelName(), tokens, finalLatency, false);

            metrics.recordTokens(tenantId, "input", (long) tokens / 2);
            metrics.recordTokens(tenantId, "output", (long) tokens / 2);
            metrics.recordRequest(tenantId, provider.getProviderName(), "success");

            metrics.stopTimer(timer, tenantId, provider.getProviderName());
            return finalResponse;

        } catch (QuotaExceededException e) {
            metrics.recordRequest(tenantId, "unknown", "quota_exceeded");
            auditLogger.logError(tenantId, userId, request.getPromptKey(), e.getMessage());
            throw e;
        } catch (Exception e) {
            metrics.recordRequest(tenantId, "unknown", "error");
            metrics.recordError(tenantId, e.getClass().getSimpleName());
            auditLogger.logError(tenantId, userId, request.getPromptKey(), e.getMessage());
            throw new AiException("AI orchestration failed", e);
        }
    }

    private String getDefaultPromptKeyForRequest(AiRequest request) {
        return "chat_assistant";
    }

    private String hashTools(List<AiProvider.ToolDefinition> tools) {
        try {
            return Integer.toHexString(objectMapper.writeValueAsString(tools).hashCode());
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    private Duration getCacheTtl(String promptKey) {
        return "product_generation".equals(promptKey) ? Duration.ofHours(24) : Duration.ofMinutes(5);
    }
}