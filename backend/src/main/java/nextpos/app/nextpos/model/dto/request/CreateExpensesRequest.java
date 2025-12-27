package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class CreateExpensesRequest {
    @NotNull private final Long warehouseId;
    @NotNull private final Long categoryId;
    @NotNull private final LocalDate date;
    @NotNull @DecimalMin(value = "0.0", inclusive = false) private final BigDecimal amount;
    private final String details;
}
