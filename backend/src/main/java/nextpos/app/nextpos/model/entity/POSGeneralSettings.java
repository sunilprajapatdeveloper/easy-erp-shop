package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "pos_general_settings", uniqueConstraints = @UniqueConstraint(columnNames = { "warehouse_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class POSGeneralSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Warehouse this POS setting belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false, unique = true)
    private Warehouse warehouse;

    /**
     * Company owning this warehouse
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /**
     * Default customer for POS transactions (e.g., Walk-in Customer)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_customer_id")
    private Customer defaultCustomer;

    /**
     * Default currency for POS transactions
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "default_currency_id", nullable = false)
    private WarehouseCurrency defaultCurrency;

    /**
     * Default payment method for POS
     */
    @Column(name = "default_payment_method", length = 20)
    private String defaultPaymentMethod;

    /**
     * Default tax inclusive/exclusive
     */
    @Column(name = "default_tax_inclusive", nullable = false)
    @Builder.Default
    private boolean defaultTaxInclusive = true;

    /**
     * Audit fields
     */
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
