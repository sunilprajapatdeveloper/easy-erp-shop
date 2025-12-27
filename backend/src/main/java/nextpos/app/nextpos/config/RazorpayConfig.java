package nextpos.app.nextpos.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "razorpay")
public class RazorpayConfig {

    /**
     * Razorpay public API key.
     */
    private String key;

    /**
     * Razorpay secret key.
     */
    private String secret;

    /**
     * Razorpay webhook secret for signature validation.
     */
    private String webhookSecret;

    /**
     * Whether to enable processing of incoming Razorpay webhooks.
     */
    private boolean enableWebhook = true;
}
