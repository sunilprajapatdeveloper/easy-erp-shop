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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
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

    @OneToMany(mappedBy = "referenceId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(nullable = false)
    private LocalDate date;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 50)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status", nullable = false, length = 50)
    private SaleStatus returnStatus;

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

    public void addProduct(SaleReturnProduct product) {
        products.add(product);
        product.setSaleReturn(this);
    }

    public void removeProduct(SaleReturnProduct product) {
        products.remove(product);
        product.setSaleReturn(null);
    }

    public void clearProducts() {
        for (SaleReturnProduct product : new ArrayList<>(products)) {
            removeProduct(product);
        }
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setReferenceType(PaymentSourceType.SALE_RETURN);
        payment.setReferenceId(this.id);
        payment.setReferenceNumber(this.referenceNumber);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setReferenceType(null);
        payment.setReferenceId(null);
        payment.setReferenceNumber(null);
    }
}
