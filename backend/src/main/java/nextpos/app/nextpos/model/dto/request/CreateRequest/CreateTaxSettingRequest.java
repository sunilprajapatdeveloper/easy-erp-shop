package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateTaxSettingRequest {

    @NotNull(message = "Tax type is required")
    private TaxType taxType;

    @NotBlank(message = "Tax name is required")
    private String name;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tax rate must be greater than 0")
    private BigDecimal rate;

    @NotNull(message = "Calculation type is required")
    private TaxCalculationType calculationType;

    @NotNull(message = "Inclusive type is required")
    private TaxInclusiveType inclusiveType;

    private boolean active;

    private String regionCode;

    private String description;

    private Long warehouseId;
}