package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateExpensesRequest {

    private Long warehouseId;

    private Long categoryId;

    private LocalDate date;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private String details;
}