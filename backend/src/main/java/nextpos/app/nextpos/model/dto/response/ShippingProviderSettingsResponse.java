package nextpos.app.nextpos.model.dto.response;

import java.time.LocalDateTime;
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
public class ShippingProviderSettingsResponse {

    private Long id;

    private Long companyId;

    private Long warehouseId;

    private ShippingProvider providerName;

    private String accountId;

    private String apiKey;

    private String apiSecret;

    private String apiEndpoint;

    private Boolean enabled;

    private String serviceRegions;

    private Map<String, Object> providerConfig;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;
}