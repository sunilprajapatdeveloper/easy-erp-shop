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
@Table(name = "sale_return")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleReturn {

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
    @JoinColumn(name = "original_sale_id", nullable = false)
    private Sale originalSale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SaleReturnProduct> products = new ArrayList<>();

    @Column(name = "order_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTax;

    @Column(name = "order_discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_discount_type", length = 20)
    private DiscountType orderDiscountType;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "rounding_amount", precision = 10, scale = 2)
    private BigDecimal roundingAmount;

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

    @Column(name = "total_amount_base_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountBaseCurrency;

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

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "return_reason", columnDefinition = "TEXT")
    private String returnReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_promotion_id")
    private Promotion appliedPromotion;

    @Column(name = "promotion_discount_amount", precision = 15, scale = 2)
    private BigDecimal promotionDiscountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_discount_type", length = 20)
    private DiscountType promotionDiscountType;

    @Column(name = "promotion_coupon_code", length = 50)
    private String promotionCouponCode;

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

        if (this.orderDiscount == null)
            this.orderDiscount = BigDecimal.ZERO;
        if (this.shippingCost == null)
            this.shippingCost = BigDecimal.ZERO;
        if (this.orderTax == null)
            this.orderTax = BigDecimal.ZERO;
        if (this.roundingAmount == null)
            this.roundingAmount = BigDecimal.ZERO;
        if (this.promotionDiscountAmount == null)
            this.promotionDiscountAmount = BigDecimal.ZERO;
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

    public void addProduct(SaleReturnProduct product) {
        products.add(product);
        product.setSaleReturn(this);
    }

    public void removeProduct(SaleReturnProduct product) {
        products.remove(product);
        product.setSaleReturn(null);
    }

    public void clearProducts() {
        new ArrayList<>(products).forEach(this::removeProduct);
    }
}