package nextpos.app.nextpos.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256 GCM encryption utility.
 * Expects a Base64-encoded 32-byte key in the property
 * 'app.encryption.secret-key'.
 * Example key generation: openssl rand -base64 32
 */
@Slf4j
@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // GCM authentication tag length
    private static final int IV_LENGTH_BYTE = 12; // 96-bit IV is recommended for GCM

    private final SecretKey secretKey;
    private final SecureRandom secureRandom;

    public EncryptionUtil(@Value("${app.encryption.secret-key}") String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        if (decodedKey.length != 32) {
            throw new IllegalArgumentException("Encryption key must be 32 bytes (256 bits) for AES-256");
        }
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        this.secureRandom = new SecureRandom();
    }

    /**
     * Encrypts plaintext and returns Base64(IV + ciphertext).
     */
    public String encrypt(String plainText) {
        if (plainText == null)
            return null;
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes());

            // Prepend IV to ciphertext
            byte[] encrypted = new byte[IV_LENGTH_BYTE + cipherText.length];
            System.arraycopy(iv, 0, encrypted, 0, IV_LENGTH_BYTE);
            System.arraycopy(cipherText, 0, encrypted, IV_LENGTH_BYTE, cipherText.length);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption error", e);
        }
    }

    /**
     * Decrypts Base64(IV + ciphertext) and returns plaintext.
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null)
            return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            if (decoded.length < IV_LENGTH_BYTE) {
                throw new IllegalArgumentException("Invalid encrypted data");
            }

            byte[] iv = new byte[IV_LENGTH_BYTE];
            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH_BYTE);

            byte[] cipherText = new byte[decoded.length - IV_LENGTH_BYTE];
            System.arraycopy(decoded, IV_LENGTH_BYTE, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption error", e);
        }
    }
}