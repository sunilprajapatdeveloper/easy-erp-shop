package nextpos.app.nextpos.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class OnlineOrderingSettingsResponse {

    private Long id;
    private Long companyId;

    private boolean enabled;
    private String orderingUrl;
    private BigDecimal minOrderValue;
    private Integer estimatedDeliveryTime;
    private boolean selfPickupEnabled;
    private boolean deliveryEnabled;
    private String integrationKey;
    private String customerNotes;

    private Map<String, Object> integrationConfig;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
