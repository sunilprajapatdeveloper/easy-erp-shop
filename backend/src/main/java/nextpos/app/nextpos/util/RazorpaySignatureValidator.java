package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.RazorpayConfig;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class RazorpaySignatureValidator {

    private final RazorpayConfig razorpayConfig;

    private static final String HMAC_SHA256 = "HmacSHA256";

    public boolean isValidSignature(String payload, String actualSignature) {
        try {
            String expectedSignature = calculateHMAC(payload, razorpayConfig.getSecret());
            boolean match = expectedSignature.equals(actualSignature);
            if (!match) {
                log.warn("Signature mismatch. Expected: {}, Actual: {}", expectedSignature, actualSignature);
            }
            return match;
        } catch (Exception e) {
            log.error("Exception occurred during Razorpay signature validation", e);
            return false;
        }
    }

    private String calculateHMAC(String data, String secret) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return new String(Base64.getEncoder().encode(rawHmac));
    }
}
