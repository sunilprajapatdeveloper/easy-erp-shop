package nextpos.app.nextpos.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Expenses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpensesResponse {

    private final Long id;
    private final Long warehouseId;
    private final Long categoryId;
    private final LocalDate date;
    private final BigDecimal amount;
    private final String details;
    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final Long updatedBy;
    private final LocalDateTime updatedAt;
    private final Long companyId;

    public ExpensesResponse(Expenses expenses) {
        this.id = expenses.getId();
        this.warehouseId = expenses.getWarehouse() != null ? expenses.getWarehouse().getId() : null;
        this.categoryId = expenses.getCategory() != null ? expenses.getCategory().getId() : null;
        this.date = expenses.getDate();
        this.amount = expenses.getAmount();
        this.details = expenses.getDetails();
        this.createdBy = expenses.getCreatedBy();
        this.createdAt = expenses.getCreatedAt();
        this.updatedBy = expenses.getUpdatedBy();
        this.updatedAt = expenses.getUpdatedAt();
        this.companyId = expenses.getCompanyId();
    }
}
