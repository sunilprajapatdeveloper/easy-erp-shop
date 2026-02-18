package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;

@Data
public class CreatePaymentGatewaySettingRequest {

    @NotNull(message = "Gateway type is required")
    private PaymentGatewayProvider gatewayType;

    @Size(max = 255, message = "Public key cannot exceed 255 characters")
    private String publicKey;

    @Size(max = 255, message = "Secret key cannot exceed 255 characters")
    private String secretKey;

    @Size(max = 100, message = "Merchant ID cannot exceed 100 characters")
    private String merchantId;

    @Size(max = 10, message = "Currency must be a valid ISO code")
    private String currency;

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;

    @Size(max = 255, message = "Webhook secret cannot exceed 255 characters")
    private String webhookSecret;

    private Boolean sandboxMode;
}