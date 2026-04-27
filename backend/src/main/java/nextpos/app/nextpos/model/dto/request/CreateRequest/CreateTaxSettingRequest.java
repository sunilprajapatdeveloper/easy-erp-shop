package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateTaxSettingRequest {

    @NotNull(message = "Tax category is required")
    private TaxCategory taxCategory;

    @NotBlank(message = "Tax name is required")
    private String name;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tax rate must be greater than 0")
    private BigDecimal rate;

    @NotNull(message = "Tax application order is required")
    private TaxApplicationOrder applicationOrder;

    @NotNull(message = "Inclusion type is required")
    private TaxInclusionType inclusionType;

    private boolean active;

    private String regionCode;

    private String description;

    private Long warehouseId;
}