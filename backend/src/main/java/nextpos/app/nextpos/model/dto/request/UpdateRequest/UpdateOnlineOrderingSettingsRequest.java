package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class UpdateOnlineOrderingSettingsRequest {

    private Long companyId;

    private Boolean enabled;

    @Size(max = 255, message = "Ordering URL must be at most 255 characters")
    private String orderingUrl;

    @Min(value = 0, message = "Minimum order value must be zero or greater")
    private BigDecimal minOrderValue;

    @Min(value = 1, message = "Estimated delivery time must be at least 1 minute")
    private Integer estimatedDeliveryTime;

    private Boolean selfPickupEnabled;

    private Boolean deliveryEnabled;

    @Size(max = 255, message = "Integration key must be at most 255 characters")
    private String integrationKey;

    @Size(max = 2000, message = "Customer notes must be at most 2000 characters")
    private String customerNotes;

    private Map<String, Object> integrationConfig;
}
