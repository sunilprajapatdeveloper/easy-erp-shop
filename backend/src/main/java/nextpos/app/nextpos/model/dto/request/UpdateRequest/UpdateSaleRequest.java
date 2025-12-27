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
import nextpos.app.nextpos.model.enums.SaleSource;
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
public class UpdateSaleRequest {

    private LocalDate date;

    private Long customerId;

    private Long warehouseId;

    @Valid
    private List<SaleProductUpdateRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal discount;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    private ShipmentStatus shipmentStatus;

    private SaleStatus saleStatus;

    private SaleSource source;

    private String note;

    @Valid
    private List<CreatePaymentRequest> payments;

    /**
     * Optional: allow currency update if sale not finalized
     */
    private Long currencyId;

    /**
     * Optional: allow updating exchange rate
     */
    @DecimalMin(value = "0.00000001", inclusive = true)
    private BigDecimal exchangeRate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleProductUpdateRequest {

        private Long productId;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitPrice;

        @Min(1)
        private Integer saleQty;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productDiscount;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productTax;
    }
}
