package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nextpos.app.nextpos.model.enums.StockEffect;

@Getter
@AllArgsConstructor
@Builder
public class CreateAdjustmentTypeRequest {
    @NotBlank private final String name;
    @NotNull private final String description;
    private final StockEffect stockEffect;
}
