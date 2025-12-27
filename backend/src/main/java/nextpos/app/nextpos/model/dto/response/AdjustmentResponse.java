package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.enums.StockEffect;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdjustmentResponse {
    private Long id;
    private WarehouseSummary warehouse;
    private LocalDate date;
    private List<ProductDetail> products;
    private String note;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class WarehouseSummary {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private Long id;
        private String code;
        private String name;
        private Integer currentQty;
        private Integer adjustedQty;
        private StockEffect stockEffect;
    }
}
