package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeScanResponse {
    private String scannerId;
    private String barcode;
    private Long productId;
    private String productName;
    private String productSku;
    private BigDecimal price;
    private Integer stockQuantity;
    private boolean success;
    private String errorMessage;
    private LocalDateTime timestamp;
}