package nextpos.app.nextpos.model.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeScanRequest {
    private String scannerId;
    private String barcode;
    private Long companyId;
    private Long warehouseId;
    private Long userId;
}
