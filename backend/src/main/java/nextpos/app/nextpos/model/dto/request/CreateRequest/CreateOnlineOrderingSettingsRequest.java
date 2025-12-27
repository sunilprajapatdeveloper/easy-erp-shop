package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class CreateOnlineOrderingSettingsRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @Size(max = 255, message = "Ordering URL must be at most 255 characters")
    private String orderingUrl;

    @Min(value = 0, message = "Minimum order value must be zero or greater")
    private BigDecimal minOrderValue;

    @Min(value = 1, message = "Estimated delivery time must be at least 1 minute")
    private Integer estimatedDeliveryTime;

    @NotNull(message = "Self pickup flag is required")
    private Boolean selfPickupEnabled;

    @NotNull(message = "Delivery flag is required")
    private Boolean deliveryEnabled;

    @Size(max = 255, message = "Integration key must be at most 255 characters")
    private String integrationKey;

    @Size(max = 2000, message = "Customer notes must be at most 2000 characters")
    private String customerNotes;

    private Map<String, Object> integrationConfig;
}