package nextpos.app.nextpos.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentSaleResponse {
    private Long saleId;
    private LocalDate date;
    private String customerName;
    private BigDecimal amount;
}