package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSaleReturnRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long originalSaleId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<SaleReturnProductRequest> products;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnTax = BigDecimal.ZERO;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnDiscount = BigDecimal.ZERO;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    private ShipmentStatus shipmentStatus;

    @NotNull
    private SaleStatus returnStatus;

    private String note;

    @Valid
    private List<CreatePaymentRequest> payments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleReturnProductRequest {

        @NotNull
        private Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitPrice;

        @NotNull
        @Min(1)
        private Integer returnQty;

        @Builder.Default
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal returnDiscount = BigDecimal.ZERO;

        @Builder.Default
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal returnTax = BigDecimal.ZERO;
    }
}
