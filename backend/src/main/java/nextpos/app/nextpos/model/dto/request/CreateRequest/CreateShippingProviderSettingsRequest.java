package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.ShippingProvider;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShippingProviderSettingsRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Provider name is required")
    private ShippingProvider providerName;

    @NotBlank(message = "Account ID is required")
    private String accountId;

    private String apiKey;

    private String apiSecret;

    private String apiEndpoint;

    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    private String serviceRegions;

    private Map<String, Object> providerConfig;
}