package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePurchaseRequest {

    private LocalDate date;

    private Long supplierId;

    private Long warehouseId;

    @Valid
    private List<PurchaseProductRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal discount;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    private ShipmentStatus shippingStatus;

    private PurchaseStatus purchaseStatus;

    private String note;

    private LocalDate expectedDeliveryDate;

    /**
     * Optional list of payments to update/create against this purchase
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

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitCost;

        @Min(1)
        private Integer purchaseQty;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productDiscount;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productTax;
    }
}
