package nextpos.app.nextpos.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class AppliedProductDiscount {
    private Long productId;
    private BigDecimal discountAmount;
    private String description;
}