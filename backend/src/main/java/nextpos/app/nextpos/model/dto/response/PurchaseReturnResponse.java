package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.PurchaseReturn;
import nextpos.app.nextpos.model.entity.PurchaseReturnProduct;
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
public class PurchaseReturnResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;
    private String supplierInvoiceNumber;

    private LocalDate date;
    private Long originalPurchaseId;
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

    private String posTerminalId;
    private Long cashierId;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public static PurchaseReturnResponse fromEntity(PurchaseReturn pr) {
        PurchaseReturnResponseBuilder builder = PurchaseReturnResponse.builder()
                .id(pr.getId())
                .referenceNumber(pr.getReferenceNumber())
                .invoiceNumber(pr.getInvoiceNumber())
                .receiptNumber(pr.getReceiptNumber())
                .supplierInvoiceNumber(pr.getSupplierInvoiceNumber())
                .date(pr.getDate())
                .originalPurchaseId(pr.getOriginalPurchase() != null ? pr.getOriginalPurchase().getId() : null)
                .supplierId(pr.getSupplier() != null ? pr.getSupplier().getId() : null)
                .supplierName(pr.getSupplier() != null ? pr.getSupplier().getName() : null)
                .warehouseId(pr.getWarehouse() != null ? pr.getWarehouse().getId() : null)
                .warehouseName(pr.getWarehouse() != null ? pr.getWarehouse().getName() : null)
                .orderTax(pr.getOrderTax())
                .orderDiscount(pr.getOrderDiscount())
                .orderDiscountType(pr.getOrderDiscountType())
                .shippingCost(pr.getShippingCost())
                .roundingAmount(pr.getRoundingAmount())
                .totalAmountTxnCurrency(pr.getTotalAmountTxnCurrency())
                .grandTotalTxnCurrency(pr.getGrandTotalTxnCurrency())
                .paidAmountTxnCurrency(pr.getPaidAmountTxnCurrency())
                .dueAmountTxnCurrency(pr.getDueAmountTxnCurrency())
                .exchangeRate(pr.getExchangeRate())
                .currencyId(pr.getCurrency() != null ? pr.getCurrency().getId() : null)
                .currencyCode(pr.getCurrency() != null ? pr.getCurrency().getCode() : null)
                .totalAmountBaseCurrency(pr.getTotalAmountBaseCurrency())
                .paidAmountBaseCurrency(pr.getPaidAmountBaseCurrency())
                .dueAmountBaseCurrency(pr.getDueAmountBaseCurrency())
                .shippingStatus(pr.getShippingStatus())
                .purchaseStatus(pr.getPurchaseStatus())
                .paymentStatus(pr.getPaymentStatus())
                .source(pr.getSource())
                .note(pr.getNote())
                .posTerminalId(pr.getPosTerminalId())
                .cashierId(pr.getCashierId())
                .createdBy(pr.getCreatedBy())
                .createdAt(pr.getCreatedAt())
                .updatedBy(pr.getUpdatedBy())
                .updatedAt(pr.getUpdatedAt())
                .companyId(pr.getCompanyId());

        if (pr.getProducts() != null) {
            builder.products(pr.getProducts().stream()
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

        public ProductDetail(PurchaseReturnProduct prp) {
            this.productId = prp.getProduct() != null ? prp.getProduct().getId() : null;
            this.productName = prp.getProduct() != null ? prp.getProduct().getName() : null;
            this.productCode = prp.getProduct() != null ? prp.getProduct().getCode() : null;
            this.productUnitCost = prp.getProductUnitCost();
            this.quantity = prp.getQuantity();
            this.discount = prp.getDiscount();
            this.subTotal = prp.getSubTotal();
            this.taxName = prp.getTaxName();
            this.taxCategory = prp.getTaxCategory();
            this.taxRate = prp.getTaxRate();
            this.taxInclusionType = prp.getTaxInclusionType();
            this.taxApplicationOrder = prp.getTaxApplicationOrder();
            this.taxAmount = prp.getTaxAmount();
            this.lineTotalTxnCurrency = (subTotal != null ? subTotal : BigDecimal.ZERO)
                    .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        }
    }
}