package nextpos.app.nextpos.ai.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.config.AiConfig;
import nextpos.app.nextpos.model.entity.TenantAiSettings;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderFactory {
    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;

    public AiProvider getProvider(TenantAiSettings settings) {
        String providerName = settings.getAiProvider();
        String baseUrl = settings.getBaseUrl();
        String apiKey = settings.getApiKey();

        AiConfig.ProviderConfig providerConfig = aiConfig.getProviders().get(providerName);
        if (providerConfig == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerName);
        }

        // Override with tenant-specific settings
        AiConfig.ProviderConfig effectiveConfig = new AiConfig.ProviderConfig();
        effectiveConfig.setApiKey(apiKey != null ? apiKey : providerConfig.getApiKey());
        effectiveConfig.setBaseUrl(baseUrl != null ? baseUrl : providerConfig.getBaseUrl());
        effectiveConfig.setDefaultModel(
                settings.getModelName() != null ? settings.getModelName() : providerConfig.getDefaultModel());

        WebClient webClient = WebClient.builder()
                .baseUrl(effectiveConfig.getBaseUrl())
                .build();

        switch (providerName) {
            case "openai":
                return new OpenAiProvider(webClient, objectMapper, effectiveConfig);
            case "deepseek":
                return new DeepSeekProvider(webClient, objectMapper, effectiveConfig);
            case "claude":
                return new ClaudeProvider(webClient, objectMapper, effectiveConfig);
            case "ollama":
                return new OllamaProvider(webClient, objectMapper);
            default:
                throw new IllegalArgumentException("Unsupported provider: " + providerName);
        }
    }
}