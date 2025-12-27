package nextpos.app.nextpos.model.dto.request;

import lombok.*;
import nextpos.app.nextpos.model.enums.ScannerType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScannerRegistrationRequest {
    private String scannerName;
    private ScannerType scannerType;
    private Long warehouseId;
    private Long assignedUserId;
    private Long companyId;
    private String ipAddress;
    private String macAddress;
}