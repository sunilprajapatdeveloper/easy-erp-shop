package nextpos.app.nextpos.ai.context;

import lombok.Data;

@Data
public class TenantContext {
    private String tenantId;
    private String userId;
    private String warehouseId; // optional
    // other context fields
}