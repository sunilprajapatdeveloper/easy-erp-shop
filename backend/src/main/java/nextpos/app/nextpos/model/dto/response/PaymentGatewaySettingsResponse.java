package nextpos.app.nextpos.model.dto.response;

import lombok.Builder;
import lombok.Data;
import nextpos.app.nextpos.model.entity.PaymentGatewaySettings;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;

@Data
@Builder
public class PaymentGatewaySettingsResponse {

    private Long id;
    private Long companyId;
    private PaymentGatewayProvider gatewayType;
    private String publicKeyMasked;
    private String secretKeyMasked;
    private String merchantIdMasked;
    private String currency;
    private boolean enabled;
    private String webhookSecretMasked;
    private boolean sandboxMode;

    // Static method to build from entity (with encryption service to mask values)
    public static PaymentGatewaySettingsResponse fromEntity(PaymentGatewaySettings entity) {
        return PaymentGatewaySettingsResponse.builder()
                .id(entity.getId())
                .companyId(entity.getCompany() != null ? entity.getCompany().getId() : null)
                .gatewayType(entity.getGatewayType())
                .publicKeyMasked(mask(entity.getPublicKey()))
                .secretKeyMasked(maskSensitive(entity.getSecretKey()))
                .merchantIdMasked(mask(entity.getMerchantId()))
                .currency(entity.getCurrency())
                .enabled(entity.isEnabled())
                .webhookSecretMasked(maskSensitive(entity.getWebhookSecret()))
                .sandboxMode(entity.isSandboxMode())
                .build();
    }

    private static String mask(String value) {
        if (value == null || value.length() < 8)
            return "••••••••";
        return value.substring(0, 4) + "••••" + value.substring(value.length() - 4);
    }

    private static String maskSensitive(String value) {
        return value == null ? null : "••••••••";
    }
}