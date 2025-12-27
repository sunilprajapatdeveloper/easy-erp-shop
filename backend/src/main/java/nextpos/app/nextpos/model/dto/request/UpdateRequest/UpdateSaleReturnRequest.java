package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
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
public class UpdateSaleReturnRequest {

    private LocalDate date;

    private Long customerId;

    private Long warehouseId;

    /**
     * Replace product list (full replace semantics).
     * For partial modifications, consider dedicated endpoints (add/remove return
     * product).
     */
    @Valid
    private List<SaleReturnProductUpdateRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal returnDiscount;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    private ShipmentStatus shipmentStatus;

    private SaleStatus returnStatus;

    private String note;

    /**
     * Refund payments can be added/updated on sale return update.
     * Handle idempotency at PaymentService layer.
     */
    @Valid
    private List<CreatePaymentRequest> payments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleReturnProductUpdateRequest {

        private Long productId;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitPrice;

        @Min(1)
        private Integer returnQty;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal returnDiscount;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal returnTax;
    }
}
