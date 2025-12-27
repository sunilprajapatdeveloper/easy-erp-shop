package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "warehouses", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "company_id" })
}, indexes = {
        @Index(name = "idx_warehouse_company", columnList = "company_id"),
        @Index(name = "idx_warehouse_currency", columnList = "currency_id"),
        @Index(name = "idx_warehouse_deleted", columnList = "is_deleted"),
        @Index(name = "idx_warehouse_default", columnList = "company_id, is_default")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(name = "zip_code", length = 15)
    private String zipCode;

    @Column(name = "is_headquarter", nullable = false)
    @Builder.Default
    private boolean headquarter = false;

    /**
     * Each warehouse may operate in a different currency.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "timezone", length = 50, nullable = false)
    @Builder.Default
    private String timezone = "UTC";

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    /**
     * Whether this warehouse applies GST/VAT/Service Tax locally.
     */
    @Builder.Default
    @Column(name = "apply_tax", nullable = false)
    private boolean applyTax = true;

    /**
     * Whether this branch applies TDS (withholding tax).
     */
    @Builder.Default
    @Column(name = "apply_tds", nullable = false)
    private boolean applyTds = false;

    /**
     * Whether stock should be tracked at this warehouse.
     */
    @Builder.Default
    @Column(name = "track_inventory", nullable = false)
    private boolean trackInventory = true;

    /**
     * Default invoice prefix for this warehouse (e.g., DELH-INV-0001).
     */
    @Column(name = "invoice_prefix", length = 20)
    private String invoicePrefix;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /**
     * Soft delete flag for enterprise-grade auditing.
     */
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    /**
     * Whether this warehouse is the default for the company.
     */
    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

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
