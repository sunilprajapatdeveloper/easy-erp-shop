package nextpos.app.nextpos.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionUsageEvent {
    private Long promotionId;
    private Long saleId;
    private Long customerId;
    private Long companyId;
    private LocalDateTime usedAt;
}