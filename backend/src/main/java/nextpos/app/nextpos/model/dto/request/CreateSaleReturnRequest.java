package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CreateSaleReturnRequest {

    @NotNull
    private final LocalDate date;

    @NotNull
    private final Long originalSaleId;

    @NotNull
    private final Long customerId;

    @NotNull
    private final Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    private final List<SaleReturnProductRequest> products;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal orderTax;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal orderDiscount;

    @NotNull
    private final DiscountType orderDiscountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal shippingCost;

    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal roundingAmount;

    @NotNull
    private final ShipmentStatus shipmentStatus;

    @NotNull
    private final SaleStatus saleStatus;

    @Builder.Default
    private final SaleSource source = SaleSource.WEB;

    private final PaymentStatus paymentStatus;

    private final String note;

    @NotNull
    private final Long currencyId;

    @NotNull
    @DecimalMin(value = "0.00000001", inclusive = true)
    private final BigDecimal exchangeRate;

    private final String couponCode;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SaleReturnProductRequest {

        @NotNull
        private final Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal productUnitPrice;

        @NotNull
        @Min(1)
        private final Integer quantity;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal discount;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal subTotal;

        @NotBlank
        @Size(max = 100)
        private final String taxName;

        @NotNull
        private final TaxCategory taxCategory;

        @NotNull
        @DecimalMin(value = "0.000", inclusive = true)
        @Digits(integer = 5, fraction = 3)
        private final BigDecimal taxRate;

        @NotNull
        private final TaxInclusionType taxInclusionType;

        @NotNull
        private final TaxApplicationOrder taxApplicationOrder;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal taxAmount;
    }
}