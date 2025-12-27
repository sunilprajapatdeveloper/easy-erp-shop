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
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
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

    @OneToMany(mappedBy = "referenceId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "order_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTax;

    @Column(name = "discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "total_amount_txn_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountTxnCurrency;

    @Column(name = "due_amount_txn_currency", precision = 15, scale = 2)
    private BigDecimal dueAmountTxnCurrency;

    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "total_amount_base_currency", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountBaseCurrency;

    @Column(name = "due_amount_base_currency", precision = 15, scale = 2)
    private BigDecimal dueAmountBaseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 50)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false, length = 50)
    private SaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleSource source;

    @Builder.Default
    @Column(name = "is_refund", nullable = false)
    private boolean isRefund = false;

    @Column(name = "pos_terminal_id", length = 100)
    private String posTerminalId;

    @Column(name = "cashier_id")
    private Long cashierId;

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

    public void addProduct(SaleProduct product) {
        products.add(product);
        product.setSale(this);
    }

    public void removeProduct(SaleProduct product) {
        products.remove(product);
        product.setSale(null);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setReferenceType(PaymentSourceType.SALE);
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
