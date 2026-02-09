package nextpos.app.nextpos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class VerificationSecurityConfig {

    @Bean("verificationTokenEncoder")
    public BCryptPasswordEncoder verificationTokenEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}