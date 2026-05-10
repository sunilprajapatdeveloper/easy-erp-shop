package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.*;

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

    @Valid
    private List<SaleReturnProductUpdateRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderDiscount;

    private DiscountType orderDiscountType;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal roundingAmount;

    private ShipmentStatus shipmentStatus;
    private SaleStatus saleStatus;
    private SaleSource source;
    private PaymentStatus paymentStatus;
    private String note;

    private Long currencyId;

    @DecimalMin(value = "0.00000001", inclusive = true)
    private BigDecimal exchangeRate;

    private String couponCode;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleReturnProductUpdateRequest {

        @NotNull
        private Long productId;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitPrice;

        @Min(1)
        private Integer quantity;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal discount;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal subTotal;

        private String taxName;
        private TaxCategory taxCategory;
        private BigDecimal taxRate;
        private TaxInclusionType taxInclusionType;
        private TaxApplicationOrder taxApplicationOrder;
        private BigDecimal taxAmount;
    }
}