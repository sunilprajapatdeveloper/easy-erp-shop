package nextpos.app.nextpos.model.dto.request.CreateRequest;

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
public class CreateSaleRequest {

    @NotNull
    private final LocalDate date;

    private final Long customerId;

    @NotNull
    private final Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    @Singular
    private final List<SaleProductRequest> products;

    @NotNull
    private final Long currencyId;

    @NotNull
    @DecimalMin("0.00000001")
    @Digits(integer = 10, fraction = 8)
    private final BigDecimal exchangeRate;

    // Manual discount (optional)
    @DecimalMin("0.0")
    private final BigDecimal manualDiscountValue;

    private final DiscountType manualDiscountType;

    @Size(max = 500)
    private final String manualDiscountReason;

    // System discount (optional)
    private final Long appliedDiscountId;

    // Promotion coupon (optional)
    @Size(max = 100)
    private final String couponCode;

    // User‑entered amounts (will be validated / overridden by promotion if free
    // shipping)
    @DecimalMin("0.0")
    private final BigDecimal shippingCost;

    @DecimalMin("0.0")
    private final BigDecimal roundingAmount;

    @DecimalMin("0.0")
    private final BigDecimal paidAmountTxnCurrency;

    @Builder.Default
    private final ShipmentStatus shipmentStatus = ShipmentStatus.PENDING;

    @Builder.Default
    private final SaleStatus saleStatus = SaleStatus.PENDING;

    @Builder.Default
    private final PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Builder.Default
    private final SaleSource source = SaleSource.WEB;

    @Size(max = 100)
    private final String posTerminalId;

    private final Long cashierId;

    private final LocalDate dueDate;

    @Size(max = 5000)
    private final String note;

    // inner product request
    @Getter
    @AllArgsConstructor
    @Builder
    public static class SaleProductRequest {
        @NotNull
        private final Long productId;

        @NotNull
        @Min(1)
        private final Integer quantity;

        /**
         * Optional: override the default price (excl. tax). If null, backend will
         * resolve it.
         */
        @DecimalMin("0.0")
        @Digits(integer = 15, fraction = 4)
        private final BigDecimal unitPriceOverride;
    }
}