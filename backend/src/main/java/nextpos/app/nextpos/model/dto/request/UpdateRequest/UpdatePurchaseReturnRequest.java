package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePurchaseReturnRequest {

    private LocalDate date;
    private Long originalPurchaseId;
    private Long supplierId;
    private Long warehouseId;

    @Size(min = 1)
    @Valid
    private List<PurchaseReturnProductRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnDiscount;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate;

    private ShipmentStatus shipmentStatus;

    private PurchaseStatus returnStatus;

    private String note;

    /**
     * Refund payments can be added/updated on purchase return update.
     * Handle idempotency at PaymentService layer.
     */
    @Valid
    private List<CreatePaymentRequest> payments;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PurchaseReturnProductRequest {

        private Long productId;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitCost;

        @Min(1)
        private Integer returnQty;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productDiscount;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productTax;
    }
}
