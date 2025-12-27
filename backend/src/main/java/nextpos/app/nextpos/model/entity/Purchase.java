package nextpos.app.nextpos.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

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

    @Builder.Default
    @OneToMany(mappedBy = "referenceId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "order_tax", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderTax;

    @Column(name = "discount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discount;

    @Column(name = "shipping_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal shippingCost;

    @Column(name = "total_amount_txn_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountTxnCurrency;

    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "total_amount_base_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountBaseCurrency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_status", length = 50, nullable = false)
    private ShipmentStatus shippingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_status", length = 50, nullable = false)
    private PurchaseStatus purchaseStatus;

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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
        for (PurchaseProduct product : new ArrayList<>(products)) {
            removeProduct(product);
        }
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setReferenceType(PaymentSourceType.PURCHASE);
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
