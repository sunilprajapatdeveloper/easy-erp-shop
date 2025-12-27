package nextpos.app.nextpos.model.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRazorpayWebhookRequest {

    private String payload;
    private String razorpaySignature;
    private String sourceIp;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CreateRazorpayWebhookRequest fromJson(String payload, String signature, String sourceIp) {
        return new CreateRazorpayWebhookRequest(payload, signature, sourceIp);
    }

    public String getEvent() {
        try {
            JsonNode root = objectMapper.readTree(this.payload);
            return root.path("event").asText(); // Extracts "payment.captured" etc.
        } catch (Exception e) {
            throw new RuntimeException("Invalid Razorpay payload JSON", e);
        }
    }
}
