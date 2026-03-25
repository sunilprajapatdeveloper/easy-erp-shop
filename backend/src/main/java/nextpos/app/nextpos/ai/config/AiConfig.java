package nextpos.app.nextpos.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    private Map<String, ProviderConfig> providers;
    private RetryConfig retry = new RetryConfig();

    @Data
    public static class ProviderConfig {
        private String apiKey;
        private String baseUrl;
        private String defaultModel;
    }

    @Data
    public static class RetryConfig {
        private int maxAttempts = 3;
        private long backoffDelayMs = 1000;
    }
}