package nextpos.app.nextpos.config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableRetry
@EnableConfigurationProperties(MailProperties.class) // Exposes the default MailProperties bean
public class EmailConfig {
    // No need to define MailProperties manually – Spring Boot auto-configures it.
    // You can inject MailProperties anywhere you need the system default SMTP
    // settings.
}