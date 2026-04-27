package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.TaxApplicationOrder;
import nextpos.app.nextpos.model.enums.TaxCategory;
import nextpos.app.nextpos.model.enums.TaxInclusionType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxSettingResponse {

    private TaxCategory taxCategory;
    private String name;
    private BigDecimal rate;
    private TaxApplicationOrder applicationOrder;
    private TaxInclusionType inclusionType;
    private boolean active;
    private String regionCode;
    private String description;
    private Long warehouseId;
}