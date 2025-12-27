package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

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
    private final List<SaleProductRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal orderTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal discount;

    @DecimalMin(value = "0.0", inclusive = true)
    private final BigDecimal shippingCost;

    @NotNull
    private final ShipmentStatus shipmentStatus;

    @NotNull
    private final SaleStatus saleStatus;

    @Builder.Default
    private final SaleSource source = SaleSource.WEB;

    private final String note;

    @Valid
    private final List<CreatePaymentRequest> payments;

    /**
     * Currency id must always be passed (system enforces valid currency reference)
     */
    @NotNull
    private final Long currencyId;

    /**
     * Exchange rate between txn currency and company base currency.
     * Mandatory since all amounts in entity are persisted in both currencies.
     */
    @NotNull
    @DecimalMin(value = "0.00000001", inclusive = true)
    private final BigDecimal exchangeRate;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SaleProductRequest {

        @NotNull
        private final Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal productUnitPrice;

        @NotNull
        @Min(1)
        private final Integer saleQty;

        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal productDiscount;

        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal productTax;
    }
}
