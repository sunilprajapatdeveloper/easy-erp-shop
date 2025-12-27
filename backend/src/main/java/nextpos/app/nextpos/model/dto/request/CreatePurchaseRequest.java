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

import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long supplierId;

    @NotNull
    private Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<PurchaseProductRequest> products;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderTax;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal discount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    @NotNull
    private ShipmentStatus shippingStatus;

    @NotNull
    private PurchaseStatus purchaseStatus;

    private String note;

    private LocalDate expectedDeliveryDate;

    /**
     * Payments made for this purchase (optional at creation).
     */
    @Valid
    private List<CreatePaymentRequest> payments;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseProductRequest {

        @NotNull
        private Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitCost;

        @NotNull
        @Min(1)
        private Integer purchaseQty;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productDiscount;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productTax;
    }
}
