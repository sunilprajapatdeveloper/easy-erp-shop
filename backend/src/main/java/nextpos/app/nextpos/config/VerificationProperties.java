package nextpos.app.nextpos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "verification")
@Data
public class VerificationProperties {

    private Token token = new Token();
    private Security security = new Security();
    private Cleanup cleanup = new Cleanup();

    @Data
    public static class Token {
        private Duration defaultExpiry = Duration.ofMinutes(15);
        private Integer defaultMaxAttempts = 5;
        private Integer length = 32;
    }

    @Data
    public static class Security {
        private Duration cooldownPeriod = Duration.ofMinutes(5);
        private Integer maxRequestsPerHour = 10;
        private Boolean enableIpRateLimiting = true;
    }

    @Data
    public static class Cleanup {
        private Duration retentionPeriod = Duration.ofDays(90);
        private String cron = "0 0 2 * * ?"; // Daily at 2 AM
    }
}
