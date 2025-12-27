package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_return")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PurchaseReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase originalPurchase;

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Builder.Default
    @OneToMany(mappedBy = "purchaseReturn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseReturnProduct> products = new ArrayList<>();

    @OneToMany(mappedBy = "referenceId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "return_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal returnTax;

    @Column(name = "return_discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal returnDiscount;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "refund_amount_txn_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal refundAmountTxnCurrency;

    @Column(name = "refund_amount_base_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal refundAmountBaseCurrency;

    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 50)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status", nullable = false, length = 50)
    private PurchaseStatus returnStatus;

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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Helper methods ---
    public void addProduct(PurchaseReturnProduct product) {
        products.add(product);
        product.setPurchaseReturn(this);
    }

    public void removeProduct(PurchaseReturnProduct product) {
        products.remove(product);
        product.setPurchaseReturn(null);
    }

    public void clearProducts() {
        for (PurchaseReturnProduct product : new ArrayList<>(products)) {
            removeProduct(product);
        }
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setReferenceType(PaymentSourceType.PURCHASE_RETURN);
        payment.setReferenceId(this.id);
        payment.setReferenceNumber(this.referenceNumber);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setReferenceType(null);
        payment.setReferenceId(null);
        payment.setReferenceNumber(null);
    }

    public void clearPayments() {
        for (Payment payment : new ArrayList<>(payments)) {
            removePayment(payment);
        }
    }
}
