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
public class CreatePurchaseReturnRequest {

    @NotNull
    private final LocalDate date;

    @NotNull
    private final Long originalPurchaseId;

    @NotNull
    private final Long supplierId;

    @NotNull
    private final Long warehouseId;

    @NotNull
    @Size(min = 1)
    @Valid
    private final List<PurchaseReturnProductRequest> products;

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
    private final ShipmentStatus shippingStatus;

    @NotNull
    private final PurchaseStatus purchaseStatus;

    private final PaymentStatus paymentStatus;

    private final String invoiceNumber;
    private final String receiptNumber;
    private final String supplierInvoiceNumber;

    @Builder.Default
    private final PurchaseSource source = PurchaseSource.MANUAL;

    private final String note;

    @NotNull
    private final Long currencyId;

    @NotNull
    @DecimalMin(value = "0.00000001", inclusive = true)
    private final BigDecimal exchangeRate;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PurchaseReturnProductRequest {

        @NotNull
        private final Long productId;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        private final BigDecimal productUnitCost;

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