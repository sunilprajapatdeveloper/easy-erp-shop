package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import nextpos.app.nextpos.model.enums.StockEffect;

@Getter
@AllArgsConstructor
@Builder
public class AdjustmentTypeResponse {
    private Long id;
    private String name;
    private String description;
    private StockEffect stockEffect;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;
}
