package nextpos.app.nextpos.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class EncryptionConfig {
    @Value("${ai.encryption.secret:changeThisSecretKey123!}")
    private String secret;

    @Value("${ai.encryption.salt:changeThisSalt567!}")
    private String salt;

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(secret, salt);
    }
}