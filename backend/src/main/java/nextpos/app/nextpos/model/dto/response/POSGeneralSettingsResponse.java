package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class POSGeneralSettingsResponse {

    private Long id;

    // Warehouse details
    private Long warehouseId;
    private String warehouseName;

    // Company details
    private Long companyId;
    private String companyName;

    // Default customer (optional, e.g., Walk-in Customer)
    private Long defaultCustomerId;
    private String defaultCustomerName;

    // Default currency (warehouse-level)
    private Long defaultCurrencyId;
    private String defaultCurrencyCode;
    private String defaultCurrencySymbol;

    // Default payment method
    private String defaultPaymentMethod;

    // Tax inclusive flag
    private boolean defaultTaxInclusive;

    // Audit metadata
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
