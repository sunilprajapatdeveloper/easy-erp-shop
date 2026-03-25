package nextpos.app.nextpos.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    private final TextEncryptor textEncryptor;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : textEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : textEncryptor.decrypt(dbData);
    }
}