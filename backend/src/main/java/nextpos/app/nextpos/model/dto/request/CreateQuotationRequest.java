package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

import nextpos.app.nextpos.model.enums.ShipmentStatus;

@Getter
@AllArgsConstructor
@Builder
public class CreateQuotationRequest {
    @NotNull private final Long customerId;
    @NotNull private final Long warehouseId;
    @NotNull @Size(min = 1) @Valid private final List<QuotationProductRequest> products;
    @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal orderTax;
    @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal discount;
    @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal shippingCost;
    @NotNull private final ShipmentStatus status;
    private final String note;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuotationProductRequest {
        @NotNull private final Long productId;
        private final String productCode;
        @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal productUnitCost;
        @NotNull @Min(0) private final Integer productStock;
        @NotNull @Min(1) private final Integer quantity;
        @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal productDiscount;
        @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal productTax;
        @NotNull @DecimalMin(value = "0.0", inclusive = true) private final BigDecimal subTotal;
    }
}
