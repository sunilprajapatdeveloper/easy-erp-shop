package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@Builder
public class CreateProductUnitRequest {
    @NotBlank private final String name;
    @NotBlank private final String shortName;
    @NotBlank private final String baseUnit;
    @NotBlank @Pattern(regexp = "[*/+-]", message = "Operator must be one of: *, /, +, -") private final String operator;
    @NotNull private final BigDecimal operatorValue;
}
