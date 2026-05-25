package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale", indexes = {
        @Index(name = "idx_sale_reference", columnList = "reference_number"),
        @Index(name = "idx_sale_company", columnList = "company_id"),
        @Index(name = "idx_sale_customer", columnList = "customer_id"),
        @Index(name = "idx_sale_date", columnList = "date"),
        @Index(name = "idx_sale_status", columnList = "sale_status"),
        @Index(name = "idx_sale_payment_status", columnList = "payment_status"),
        @Index(name = "idx_sale_applied_promotion", columnList = "applied_promotion_id"),
        @Index(name = "idx_sale_applied_discount", columnList = "applied_discount_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(name = "invoice_number", unique = true, length = 100)
    private String invoiceNumber;

    @Column(name = "receipt_number", unique = true, length = 100)
    private String receiptNumber;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SaleProduct> products = new ArrayList<>();

    @Column(name = "total_tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalTaxAmount;

    @Column(name = "order_discount", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_discount_type", length = 20)
    private DiscountType orderDiscountType;

    @Column(name = "order_discount_value", precision = 15, scale = 2)
    private BigDecimal orderDiscountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_source", length = 30)
    private DiscountSource discountSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_discount_id")
    private Discount appliedDiscount;

    @Column(name = "discount_name", length = 255)
    private String discountName;

    @Column(name = "discount_code", length = 100)
    private String discountCode;

    @Column(name = "discount_description", columnDefinition = "TEXT")
    private String discountDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_promotion_id")
    private Promotion appliedPromotion;

    @Column(name = "promotion_discount_amount", precision = 15, scale = 2)
    private BigDecimal promotionDiscountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_discount_type", length = 20)
    private DiscountType promotionDiscountType;

    @Column(name = "promotion_discount_value", precision = 15, scale = 2)
    private BigDecimal promotionDiscountValue;

    @Column(name = "promotion_coupon_code", length = 100)
    private String promotionCouponCode;

    @Column(name = "promotion_name", length = 255)
    private String promotionName;

    @Column(name = "promotion_code", length = 100)
    private String promotionCode;

    @Column(name = "promotion_description", columnDefinition = "TEXT")
    private String promotionDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", length = 30)
    private PromotionType promotionType;

    @Column(name = "total_discount_amount", precision = 15, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "shipping_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "rounding_amount", precision = 15, scale = 2)
    private BigDecimal roundingAmount;

    @Column(name = "subtotal_amount_txn_currency", precision = 15, scale = 2)
    private BigDecimal subtotalAmountTxnCurrency;

    @Column(name = "total_amount_txn_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountTxnCurrency;

    @Column(name = "grand_total_txn_currency", precision = 15, scale = 2)
    private BigDecimal grandTotalTxnCurrency;

    @Column(name = "paid_amount_txn_currency", precision = 15, scale = 2)
    private BigDecimal paidAmountTxnCurrency;

    @Column(name = "due_amount_txn_currency", precision = 15, scale = 2)
    private BigDecimal dueAmountTxnCurrency;

    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "subtotal_amount_base_currency", precision = 15, scale = 2)
    private BigDecimal subtotalAmountBaseCurrency;

    @Column(name = "total_amount_base_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountBaseCurrency;

    @Column(name = "grand_total_base_currency", precision = 15, scale = 2)
    private BigDecimal grandTotalBaseCurrency;

    @Column(name = "paid_amount_base_currency", precision = 15, scale = 2)
    private BigDecimal paidAmountBaseCurrency;

    @Column(name = "due_amount_base_currency", precision = 15, scale = 2)
    private BigDecimal dueAmountBaseCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 50)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false, length = 50)
    private SaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 30)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleSource source;

    @Column(name = "pos_terminal_id", length = 100)
    private String posTerminalId;

    @Column(name = "cashier_id")
    private Long cashierId;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_by", updatable = false, nullable = false)
    private Long createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", updatable = false, nullable = false)
    private Long companyId;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.totalTaxAmount == null)
            this.totalTaxAmount = BigDecimal.ZERO;

        if (this.orderDiscount == null)
            this.orderDiscount = BigDecimal.ZERO;

        if (this.orderDiscountValue == null)
            this.orderDiscountValue = BigDecimal.ZERO;

        if (this.promotionDiscountAmount == null)
            this.promotionDiscountAmount = BigDecimal.ZERO;

        if (this.promotionDiscountValue == null)
            this.promotionDiscountValue = BigDecimal.ZERO;

        if (this.totalDiscountAmount == null)
            this.totalDiscountAmount = BigDecimal.ZERO;

        if (this.shippingCost == null)
            this.shippingCost = BigDecimal.ZERO;

        if (this.roundingAmount == null)
            this.roundingAmount = BigDecimal.ZERO;

        if (this.paidAmountTxnCurrency == null)
            this.paidAmountTxnCurrency = BigDecimal.ZERO;

        if (this.dueAmountTxnCurrency == null)
            this.dueAmountTxnCurrency = BigDecimal.ZERO;

        if (this.paidAmountBaseCurrency == null)
            this.paidAmountBaseCurrency = BigDecimal.ZERO;

        if (this.dueAmountBaseCurrency == null)
            this.dueAmountBaseCurrency = BigDecimal.ZERO;

        if (this.shipmentStatus == null)
            this.shipmentStatus = ShipmentStatus.PENDING;

        if (this.saleStatus == null)
            this.saleStatus = SaleStatus.PENDING;

        if (this.paymentStatus == null)
            this.paymentStatus = PaymentStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addProduct(SaleProduct product) {
        products.add(product);
        product.setSale(this);
    }

    public void removeProduct(SaleProduct product) {
        products.remove(product);
        product.setSale(null);
    }
}