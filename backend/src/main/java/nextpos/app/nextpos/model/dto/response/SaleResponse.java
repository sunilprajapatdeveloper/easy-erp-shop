package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;
    private LocalDate date;

    private Long customerId;
    private String customerName;

    private Long warehouseId;
    private String warehouseName;

    private List<SaleProductResponse> products;

    // Transaction currency fields
    private BigDecimal subtotalAmountTxnCurrency;
    private BigDecimal totalTaxAmount;
    private BigDecimal orderDiscount;
    private DiscountType orderDiscountType;
    private DiscountSource discountSource;
    private Long appliedDiscountId;
    private String discountName;
    private String discountCode;
    private String discountDescription;
    private BigDecimal promotionDiscountAmount;
    private DiscountType promotionDiscountType;
    private BigDecimal promotionDiscountValue;
    private String promotionCouponCode;
    private String promotionName;
    private String promotionCode;
    private String promotionDescription;
    private PromotionType promotionType;
    private BigDecimal totalDiscountAmount;
    private BigDecimal shippingCost;
    private BigDecimal roundingAmount;
    private BigDecimal totalAmountTxnCurrency;
    private BigDecimal grandTotalTxnCurrency;
    private BigDecimal paidAmountTxnCurrency;
    private BigDecimal dueAmountTxnCurrency;
    private BigDecimal exchangeRate;
    private Long currencyId;
    private String currencyCode;

    // Base currency fields
    private BigDecimal subtotalAmountBaseCurrency;
    private BigDecimal totalAmountBaseCurrency;
    private BigDecimal grandTotalBaseCurrency;
    private BigDecimal paidAmountBaseCurrency;
    private BigDecimal dueAmountBaseCurrency;

    private ShipmentStatus shipmentStatus;
    private SaleStatus saleStatus;
    private PaymentStatus paymentStatus;
    private SaleSource source;

    private String posTerminalId;
    private Long cashierId;
    private LocalDate dueDate;
    private String note;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    // ---------- static factory ----------
    public static SaleResponse fromEntity(Sale sale) {
        SaleResponseBuilder b = SaleResponse.builder()
                .id(sale.getId())
                .referenceNumber(sale.getReferenceNumber())
                .invoiceNumber(sale.getInvoiceNumber())
                .receiptNumber(sale.getReceiptNumber())
                .date(sale.getDate())
                .customerId(sale.getCustomer() != null ? sale.getCustomer().getId() : null)
                .customerName(sale.getCustomer() != null ? sale.getCustomer().getName() : null)
                .warehouseId(sale.getWarehouse().getId())
                .warehouseName(sale.getWarehouse().getName())
                .subtotalAmountTxnCurrency(sale.getSubtotalAmountTxnCurrency())
                .totalTaxAmount(sale.getTotalTaxAmount())
                .orderDiscount(sale.getOrderDiscount())
                .orderDiscountType(sale.getOrderDiscountType())
                .discountSource(sale.getDiscountSource())
                .appliedDiscountId(sale.getAppliedDiscount() != null ? sale.getAppliedDiscount().getId() : null)
                .discountName(sale.getDiscountName())
                .discountCode(sale.getDiscountCode())
                .discountDescription(sale.getDiscountDescription())
                .promotionDiscountAmount(sale.getPromotionDiscountAmount())
                .promotionDiscountType(sale.getPromotionDiscountType())
                .promotionDiscountValue(sale.getPromotionDiscountValue())
                .promotionCouponCode(sale.getPromotionCouponCode())
                .promotionName(sale.getPromotionName())
                .promotionCode(sale.getPromotionCode())
                .promotionDescription(sale.getPromotionDescription())
                .promotionType(sale.getPromotionType())
                .totalDiscountAmount(sale.getTotalDiscountAmount())
                .shippingCost(sale.getShippingCost())
                .roundingAmount(sale.getRoundingAmount())
                .totalAmountTxnCurrency(sale.getTotalAmountTxnCurrency())
                .grandTotalTxnCurrency(sale.getGrandTotalTxnCurrency())
                .paidAmountTxnCurrency(sale.getPaidAmountTxnCurrency())
                .dueAmountTxnCurrency(sale.getDueAmountTxnCurrency())
                .exchangeRate(sale.getExchangeRate())
                .currencyId(sale.getCurrency().getId())
                .currencyCode(sale.getCurrency().getCode())
                .subtotalAmountBaseCurrency(sale.getSubtotalAmountBaseCurrency())
                .totalAmountBaseCurrency(sale.getTotalAmountBaseCurrency())
                .grandTotalBaseCurrency(sale.getGrandTotalBaseCurrency())
                .paidAmountBaseCurrency(sale.getPaidAmountBaseCurrency())
                .dueAmountBaseCurrency(sale.getDueAmountBaseCurrency())
                .shipmentStatus(sale.getShipmentStatus())
                .saleStatus(sale.getSaleStatus())
                .paymentStatus(sale.getPaymentStatus())
                .source(sale.getSource())
                .posTerminalId(sale.getPosTerminalId())
                .cashierId(sale.getCashierId())
                .dueDate(sale.getDueDate())
                .note(sale.getNote())
                .createdBy(sale.getCreatedBy())
                .createdAt(sale.getCreatedAt())
                .updatedBy(sale.getUpdatedBy())
                .updatedAt(sale.getUpdatedAt())
                .companyId(sale.getCompanyId());

        if (sale.getProducts() != null) {
            b.products(sale.getProducts().stream()
                    .map(SaleProductResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return b.build();
    }
}