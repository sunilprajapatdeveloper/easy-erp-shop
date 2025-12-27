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
@Table(name = "company_currencies", indexes = {
        @Index(name = "idx_company_currency_company", columnList = "company_id"),
        @Index(name = "idx_company_currency_currency", columnList = "currency_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uq_company_currency", columnNames = { "company_id", "currency_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Global currency reference
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Version
    private Long version;

    /**
     * Optional: enforce some rules before persist/update
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        if (company == null) {
            throw new IllegalStateException("Company must be set for CompanyCurrency");
        }
    }
}
