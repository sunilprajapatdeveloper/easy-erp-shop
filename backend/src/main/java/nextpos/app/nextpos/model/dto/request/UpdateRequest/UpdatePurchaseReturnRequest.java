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
public class UpdatePurchaseReturnRequest {

    private LocalDate date;
    private Long originalPurchaseId;
    private Long supplierId;
    private Long warehouseId;

    @Valid
    private List<PurchaseReturnProductRequest> products;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderTax;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal orderDiscount;

    private DiscountType orderDiscountType;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal roundingAmount;

    private ShipmentStatus shippingStatus;
    private PurchaseStatus purchaseStatus;
    private PaymentStatus paymentStatus;

    private String invoiceNumber;
    private String receiptNumber;
    private String supplierInvoiceNumber;

    private PurchaseSource source;

    private String note;

    private Long currencyId;

    @DecimalMin(value = "0.00000001", inclusive = true)
    private BigDecimal exchangeRate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseReturnProductRequest {

        @NotNull
        private Long productId;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal productUnitCost;

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