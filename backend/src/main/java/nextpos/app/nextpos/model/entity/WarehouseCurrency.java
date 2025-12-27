package nextpos.app.nextpos.model.entity;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.CurrencyStatus;
import nextpos.app.nextpos.model.enums.RoundingMode;

@Entity
@Table(name = "warehouse_currencies", indexes = {
        @Index(name = "idx_warehouse_currency_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_warehouse_currency_currency", columnList = "currency_id"),
        @Index(name = "idx_warehouse_currency_company", columnList = "company_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uq_warehouse_currency", columnNames = { "warehouse_id", "currency_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to global currency
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    // Tenant-specific settings
    @Column(nullable = false)
    @Builder.Default
    private Integer decimalPlaces = 2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    @Column(nullable = false)
    @Builder.Default
    private boolean defaultCurrency = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CurrencyStatus status;

    // Reference to company (for multi-tenant isolation)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Reference to warehouse
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Version
    private Long version;

    /**
     * Optional: enforce some rules before persist/update
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        if (company == null || warehouse == null) {
            throw new IllegalStateException("Both company and warehouse must be set for WarehouseCurrency");
        }
    }
}
