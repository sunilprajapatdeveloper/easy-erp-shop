package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.ProductUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ProductUnitResponse {
    private Long id;
    private String name;
    private String shortName;
    private String baseUnit;
    private String operator;
    private BigDecimal operatorValue;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public ProductUnitResponse(ProductUnit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.shortName = unit.getShortName();
        this.baseUnit = unit.getBaseUnit();
        this.operator = unit.getOperator();
        this.operatorValue = unit.getOperatorValue();
        this.createdBy = unit.getCreatedBy();
        this.createdAt = unit.getCreatedAt();
        this.updatedBy = unit.getUpdatedBy();
        this.updatedAt = unit.getUpdatedAt();
        this.companyId = unit.getCompanyId();
    }
}
