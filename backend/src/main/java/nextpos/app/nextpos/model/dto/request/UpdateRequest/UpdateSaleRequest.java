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
public class UpdateSaleRequest {

    private LocalDate date;
    private Long customerId;
    private Long warehouseId;

    @Valid
    private List<SaleProductUpdateRequest> products;

    // Manual discount
    private BigDecimal manualDiscountValue;
    private DiscountType manualDiscountType;
    private String manualDiscountReason;

    // system discount
    private Long appliedDiscountId;

    // promotion coupon code
    private String couponCode;

    private BigDecimal shippingCost;
    private BigDecimal roundingAmount;

    @Size(max = 100)
    private String posTerminalId;
    private Long cashierId;
    private LocalDate dueDate;

    private ShipmentStatus shipmentStatus;
    private SaleStatus saleStatus;
    private PaymentStatus paymentStatus;
    private SaleSource source;
    private String note;

    private Long currencyId;
    private BigDecimal exchangeRate;

    // inner product update request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleProductUpdateRequest {
        @NotNull
        private Long productId;
        @Min(1)
        private Integer quantity;
        @DecimalMin("0.0")
        @Digits(integer = 15, fraction = 4)
        private BigDecimal unitPriceOverride;
    }
}