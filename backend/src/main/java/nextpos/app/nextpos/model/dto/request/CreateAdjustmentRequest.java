package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.enums.StockEffect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CreateAdjustmentRequest {
    @NotNull private final Long warehouseId;
    @NotNull private final LocalDate date;
    @NotNull @Size(min = 1) private final List<AdjustmentProductRequest> products;
    private final String note;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AdjustmentProductRequest {
        @NotNull private final Long productId;
        @NotNull private final Integer adjustedQty;
        @NotNull private final StockEffect stockEffect;
    }
}
