package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.TaxCalculationType;
import nextpos.app.nextpos.model.enums.TaxInclusiveType;
import nextpos.app.nextpos.model.enums.TaxType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxSettingResponse {

    private TaxType taxType;
    private String name;
    private BigDecimal rate;
    private TaxCalculationType calculationType;
    private TaxInclusiveType inclusiveType;
    private boolean active;
    private String regionCode;
    private String description;
    private Long warehouseId;
}