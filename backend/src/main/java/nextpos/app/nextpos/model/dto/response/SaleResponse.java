package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleProduct;
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
public class SaleResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;

    private LocalDate date;
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
    private String note;
    private SaleSource source;

    private String posTerminalId;
    private Long cashierId;
    private LocalDate dueDate;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public static SaleResponse fromEntity(Sale sale) {
        SaleResponseBuilder builder = SaleResponse.builder()
                .id(sale.getId())
                .referenceNumber(sale.getReferenceNumber())
                .invoiceNumber(sale.getInvoiceNumber())
                .receiptNumber(sale.getReceiptNumber())
                .date(sale.getDate())
                .customerId(sale.getCustomer() != null ? sale.getCustomer().getId() : null)
                .warehouseId(sale.getWarehouse() != null ? sale.getWarehouse().getId() : null)
                .companyId(sale.getCompanyId())
                .orderTax(sale.getOrderTax())
                .orderDiscount(sale.getOrderDiscount())
                .orderDiscountType(sale.getOrderDiscountType())
                .shippingCost(sale.getShippingCost())
                .roundingAmount(sale.getRoundingAmount())
                .totalAmountTxnCurrency(sale.getTotalAmountTxnCurrency())
                .paidAmountTxnCurrency(sale.getPaidAmountTxnCurrency())
                .dueAmountTxnCurrency(sale.getDueAmountTxnCurrency())
                .grandTotalTxnCurrency(sale.getGrandTotalTxnCurrency())
                .exchangeRate(sale.getExchangeRate())
                .currencyId(sale.getCurrency() != null ? sale.getCurrency().getId() : null)
                .currencyCode(sale.getCurrency() != null ? sale.getCurrency().getCode() : null)
                .totalAmountBaseCurrency(sale.getTotalAmountBaseCurrency())
                .paidAmountBaseCurrency(sale.getPaidAmountBaseCurrency())
                .dueAmountBaseCurrency(sale.getDueAmountBaseCurrency())
                .appliedPromotionId(sale.getAppliedPromotion() != null ? sale.getAppliedPromotion().getId() : null)
                .appliedPromotionName(sale.getAppliedPromotion() != null ? sale.getAppliedPromotion().getName() : null)
                .promotionDiscountAmount(sale.getPromotionDiscountAmount())
                .promotionDiscountType(sale.getPromotionDiscountType())
                .promotionCouponCode(sale.getPromotionCouponCode())
                .shipmentStatus(sale.getShipmentStatus())
                .saleStatus(sale.getSaleStatus())
                .note(sale.getNote())
                .source(sale.getSource())
                .posTerminalId(sale.getPosTerminalId())
                .cashierId(sale.getCashierId())
                .dueDate(sale.getDueDate())
                .createdBy(sale.getCreatedBy())
                .createdAt(sale.getCreatedAt())
                .updatedBy(sale.getUpdatedBy())
                .updatedAt(sale.getUpdatedAt());

        if (sale.getProducts() != null) {
            builder.products(sale.getProducts().stream()
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

        public ProductDetail(SaleProduct sp) {
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
