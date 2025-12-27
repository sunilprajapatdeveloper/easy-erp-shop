package nextpos.app.nextpos.model.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private BigDecimal sales;
    private BigDecimal purchases;
    private BigDecimal salesReturn;
    private BigDecimal purchaseReturn;
}