package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreatePurchaseReturnRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long originalPurchaseId;

    @NotNull
    private Long supplierId;

    @NotNull
    private Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<PurchaseReturnProductRequest> products;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnTax = BigDecimal.ZERO;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnDiscount = BigDecimal.ZERO;

    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    /**
     * Needed to compute base-currency refund amounts.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate;

    @NotNull
    private ShipmentStatus shipmentStatus;

    @NotNull
    private PurchaseStatus returnStatus;

    private String note;

    /**
     * Optional: capture refund payments at creation time.
     */
    @Valid
    private List<CreatePaymentRequest> payments;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseReturnProductRequest {

        @NotNull
        private Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitCost;

        @NotNull
        @Min(1)
        private Integer returnQty;

        @Builder.Default
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productDiscount = BigDecimal.ZERO;

        @Builder.Default
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productTax = BigDecimal.ZERO;
    }
}
