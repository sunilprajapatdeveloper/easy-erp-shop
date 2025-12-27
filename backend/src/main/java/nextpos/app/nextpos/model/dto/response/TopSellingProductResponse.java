package nextpos.app.nextpos.model.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopSellingProductResponse {
    private Long productId;
    private String productName;
    private Long quantitySold;
    private BigDecimal revenue;
}