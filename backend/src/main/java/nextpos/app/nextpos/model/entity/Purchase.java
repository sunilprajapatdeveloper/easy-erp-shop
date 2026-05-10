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
@Table(name = "purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(name = "invoice_number", unique = true, length = 100)
    private String invoiceNumber;

    @Column(name = "receipt_number", unique = true, length = 100)
    private String receiptNumber;

    @Column(name = "supplier_invoice_number", length = 100)
    private String supplierInvoiceNumber;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Builder.Default
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseProduct> products = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "order_tax", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderTax;

    @Column(name = "order_discount", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_discount_type", length = 20)
    private DiscountType orderDiscountType;

    @Column(name = "shipping_cost", precision = 10, scale = 2, nullable = false)
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
    @Column(name = "shipping_status", length = 50, nullable = false)
    private ShipmentStatus shippingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_status", length = 50, nullable = false)
    private PurchaseStatus purchaseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 30)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 50)
    private PurchaseSource source;

    @Column(name = "pos_terminal_id", length = 100)
    private String posTerminalId;

    @Column(name = "cashier_id")
    private Long cashierId;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false, updatable = false)
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
        if (this.shippingStatus == null)
            this.shippingStatus = ShipmentStatus.PENDING;
        if (this.purchaseStatus == null)
            this.purchaseStatus = PurchaseStatus.PENDING;
        if (this.paymentStatus == null)
            this.paymentStatus = PaymentStatus.PENDING;
        if (this.source == null)
            this.source = PurchaseSource.MANUAL;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addProduct(PurchaseProduct product) {
        products.add(product);
        product.setPurchase(this);
    }

    public void removeProduct(PurchaseProduct product) {
        products.remove(product);
        product.setPurchase(null);
    }

    public void clearProducts() {
        new ArrayList<>(products).forEach(this::removeProduct);
    }
}