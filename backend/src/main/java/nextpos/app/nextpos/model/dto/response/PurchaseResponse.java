package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.Purchase;
import nextpos.app.nextpos.model.entity.PurchaseProduct;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;
    private String supplierInvoiceNumber;

    private LocalDate date;

    private Long supplierId;
    private String supplierName;

    private Long warehouseId;
    private String warehouseName;

    private List<ProductDetail> products;

    private BigDecimal orderTax;
    private BigDecimal orderDiscount;
    private DiscountType orderDiscountType;
    private BigDecimal shippingCost;
    private BigDecimal roundingAmount;

    private BigDecimal totalAmountTxnCurrency;
    private BigDecimal grandTotalTxnCurrency;
    private BigDecimal paidAmountTxnCurrency;
    private BigDecimal dueAmountTxnCurrency;

    private BigDecimal exchangeRate;
    private Long currencyId;
    private String currencyCode;
    private BigDecimal totalAmountBaseCurrency;
    private BigDecimal paidAmountBaseCurrency;
    private BigDecimal dueAmountBaseCurrency;

    private ShipmentStatus shippingStatus;
    private PurchaseStatus purchaseStatus;
    private PaymentStatus paymentStatus;

    private PurchaseSource source;
    private String note;
    private LocalDate expectedDeliveryDate;

    private String posTerminalId;
    private Long cashierId;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public static PurchaseResponse fromEntity(Purchase purchase) {
        PurchaseResponseBuilder builder = PurchaseResponse.builder()
                .id(purchase.getId())
                .referenceNumber(purchase.getReferenceNumber())
                .invoiceNumber(purchase.getInvoiceNumber())
                .receiptNumber(purchase.getReceiptNumber())
                .supplierInvoiceNumber(purchase.getSupplierInvoiceNumber())
                .date(purchase.getDate())
                .supplierId(purchase.getSupplier() != null ? purchase.getSupplier().getId() : null)
                .supplierName(purchase.getSupplier() != null ? purchase.getSupplier().getName() : null)
                .warehouseId(purchase.getWarehouse() != null ? purchase.getWarehouse().getId() : null)
                .warehouseName(purchase.getWarehouse() != null ? purchase.getWarehouse().getName() : null)
                .orderTax(purchase.getOrderTax())
                .orderDiscount(purchase.getOrderDiscount())
                .orderDiscountType(purchase.getOrderDiscountType())
                .shippingCost(purchase.getShippingCost())
                .roundingAmount(purchase.getRoundingAmount())
                .totalAmountTxnCurrency(purchase.getTotalAmountTxnCurrency())
                .grandTotalTxnCurrency(purchase.getGrandTotalTxnCurrency())
                .paidAmountTxnCurrency(purchase.getPaidAmountTxnCurrency())
                .dueAmountTxnCurrency(purchase.getDueAmountTxnCurrency())
                .exchangeRate(purchase.getExchangeRate())
                .currencyId(purchase.getCurrency() != null ? purchase.getCurrency().getId() : null)
                .currencyCode(purchase.getCurrency() != null ? purchase.getCurrency().getCode() : null)
                .totalAmountBaseCurrency(purchase.getTotalAmountBaseCurrency())
                .paidAmountBaseCurrency(purchase.getPaidAmountBaseCurrency())
                .dueAmountBaseCurrency(purchase.getDueAmountBaseCurrency())
                .shippingStatus(purchase.getShippingStatus())
                .purchaseStatus(purchase.getPurchaseStatus())
                .paymentStatus(purchase.getPaymentStatus())
                .source(purchase.getSource())
                .note(purchase.getNote())
                .expectedDeliveryDate(purchase.getExpectedDeliveryDate())
                .posTerminalId(purchase.getPosTerminalId())
                .cashierId(purchase.getCashierId())
                .createdBy(purchase.getCreatedBy())
                .createdAt(purchase.getCreatedAt())
                .updatedBy(purchase.getUpdatedBy())
                .updatedAt(purchase.getUpdatedAt())
                .companyId(purchase.getCompanyId());

        if (purchase.getProducts() != null) {
            builder.products(purchase.getProducts().stream()
                    .map(ProductDetail::new)
                    .collect(Collectors.toList()));
        }
        return builder.build();
    }

    @Getter
    public static class ProductDetail {
        private final Long productId;
        private final String productName;
        private final String productCode;
        private final BigDecimal productUnitCost;
        private final Integer quantity;
        private final BigDecimal discount;
        private final BigDecimal subTotal;

        private final String taxName;
        private final TaxCategory taxCategory;
        private final BigDecimal taxRate;
        private final TaxInclusionType taxInclusionType;
        private final TaxApplicationOrder taxApplicationOrder;
        private final BigDecimal taxAmount;

        private final BigDecimal lineTotalTxnCurrency;

        public ProductDetail(PurchaseProduct pp) {
            this.productId = pp.getProduct() != null ? pp.getProduct().getId() : null;
            this.productName = pp.getProduct() != null ? pp.getProduct().getName() : null;
            this.productCode = pp.getProduct() != null ? pp.getProduct().getCode() : null;
            this.productUnitCost = pp.getProductUnitCost();
            this.quantity = pp.getQuantity();
            this.discount = pp.getDiscount();
            this.subTotal = pp.getSubTotal();
            this.taxName = pp.getTaxName();
            this.taxCategory = pp.getTaxCategory();
            this.taxRate = pp.getTaxRate();
            this.taxInclusionType = pp.getTaxInclusionType();
            this.taxApplicationOrder = pp.getTaxApplicationOrder();
            this.taxAmount = pp.getTaxAmount();
            this.lineTotalTxnCurrency = (subTotal != null ? subTotal : BigDecimal.ZERO)
                    .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        }
    }
}