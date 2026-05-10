package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.SaleReturn;
import nextpos.app.nextpos.model.entity.SaleReturnProduct;
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
public class SaleReturnResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;

    private LocalDate date;
    private Long originalSaleId;
    private Long customerId;
    private Long warehouseId;

    private List<ProductDetail> products;

    private BigDecimal orderTax;
    private BigDecimal orderDiscount;
    private DiscountType orderDiscountType;
    private BigDecimal shippingCost;
    private BigDecimal roundingAmount;

    private BigDecimal totalAmountTxnCurrency;
    private BigDecimal paidAmountTxnCurrency;
    private BigDecimal dueAmountTxnCurrency;
    private BigDecimal grandTotalTxnCurrency;

    private BigDecimal exchangeRate;
    private Long currencyId;
    private String currencyCode;
    private BigDecimal totalAmountBaseCurrency;
    private BigDecimal paidAmountBaseCurrency;
    private BigDecimal dueAmountBaseCurrency;

    private Long appliedPromotionId;
    private String appliedPromotionName;
    private BigDecimal promotionDiscountAmount;
    private DiscountType promotionDiscountType;
    private String promotionCouponCode;

    private ShipmentStatus shipmentStatus;
    private SaleStatus saleStatus;
    private PaymentStatus paymentStatus;
    private SaleSource source;

    private String note;
    private String returnReason;

    private String posTerminalId;
    private Long cashierId;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public static SaleReturnResponse fromEntity(SaleReturn sr) {
        SaleReturnResponseBuilder builder = SaleReturnResponse.builder()
                .id(sr.getId())
                .referenceNumber(sr.getReferenceNumber())
                .invoiceNumber(sr.getInvoiceNumber())
                .receiptNumber(sr.getReceiptNumber())
                .date(sr.getDate())
                .originalSaleId(sr.getOriginalSale() != null ? sr.getOriginalSale().getId() : null)
                .customerId(sr.getCustomer() != null ? sr.getCustomer().getId() : null)
                .warehouseId(sr.getWarehouse() != null ? sr.getWarehouse().getId() : null)
                .companyId(sr.getCompanyId())
                .orderTax(sr.getOrderTax())
                .orderDiscount(sr.getOrderDiscount())
                .orderDiscountType(sr.getOrderDiscountType())
                .shippingCost(sr.getShippingCost())
                .roundingAmount(sr.getRoundingAmount())
                .totalAmountTxnCurrency(sr.getTotalAmountTxnCurrency())
                .paidAmountTxnCurrency(sr.getPaidAmountTxnCurrency())
                .dueAmountTxnCurrency(sr.getDueAmountTxnCurrency())
                .grandTotalTxnCurrency(sr.getGrandTotalTxnCurrency())
                .exchangeRate(sr.getExchangeRate())
                .currencyId(sr.getCurrency() != null ? sr.getCurrency().getId() : null)
                .currencyCode(sr.getCurrency() != null ? sr.getCurrency().getCode() : null)
                .totalAmountBaseCurrency(sr.getTotalAmountBaseCurrency())
                .paidAmountBaseCurrency(sr.getPaidAmountBaseCurrency())
                .dueAmountBaseCurrency(sr.getDueAmountBaseCurrency())
                .appliedPromotionId(sr.getAppliedPromotion() != null ? sr.getAppliedPromotion().getId() : null)
                .appliedPromotionName(sr.getAppliedPromotion() != null ? sr.getAppliedPromotion().getName() : null)
                .promotionDiscountAmount(sr.getPromotionDiscountAmount())
                .promotionDiscountType(sr.getPromotionDiscountType())
                .promotionCouponCode(sr.getPromotionCouponCode())
                .shipmentStatus(sr.getShipmentStatus())
                .saleStatus(sr.getSaleStatus())
                .paymentStatus(sr.getPaymentStatus())
                .source(sr.getSource())
                .note(sr.getNote())
                .returnReason(sr.getReturnReason())
                .posTerminalId(sr.getPosTerminalId())
                .cashierId(sr.getCashierId())
                .createdBy(sr.getCreatedBy())
                .createdAt(sr.getCreatedAt())
                .updatedBy(sr.getUpdatedBy())
                .updatedAt(sr.getUpdatedAt());

        if (sr.getProducts() != null) {
            builder.products(sr.getProducts().stream()
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
        private final BigDecimal productUnitPrice;
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

        public ProductDetail(SaleReturnProduct sp) {
            this.productId = sp.getProduct() != null ? sp.getProduct().getId() : null;
            this.productName = sp.getProduct() != null ? sp.getProduct().getName() : null;
            this.productCode = sp.getProduct() != null ? sp.getProduct().getCode() : null;
            this.productUnitPrice = sp.getProductUnitPrice();
            this.quantity = sp.getQuantity();
            this.discount = sp.getDiscount();
            this.subTotal = sp.getSubTotal();
            this.taxName = sp.getTaxName();
            this.taxCategory = sp.getTaxCategory();
            this.taxRate = sp.getTaxRate();
            this.taxInclusionType = sp.getTaxInclusionType();
            this.taxApplicationOrder = sp.getTaxApplicationOrder();
            this.taxAmount = sp.getTaxAmount();
            this.lineTotalTxnCurrency = (subTotal != null ? subTotal : BigDecimal.ZERO)
                    .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        }
    }
}