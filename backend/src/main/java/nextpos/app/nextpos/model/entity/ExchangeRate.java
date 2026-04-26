package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import nextpos.app.nextpos.model.enums.ExternalExchangeRateProvider;
import nextpos.app.nextpos.model.enums.ExchangeRateSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Entity
@Table(name = "exchange_rates", uniqueConstraints = {
        @UniqueConstraint(name = "uk_exchange_rate_unique", columnNames = { "base_currency_id", "target_currency_id",
                "level", "company_id", "warehouse_id" })
}, indexes = {
        @Index(name = "idx_exchange_rate_base_target", columnList = "base_currency_id, target_currency_id"),
        @Index(name = "idx_exchange_rate_level", columnList = "level, company_id, warehouse_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_currency_id", nullable = false)
    private Currency targetCurrency;

    @Column(precision = 19, scale = 8, nullable = false)
    private BigDecimal rate;

    @Column(precision = 19, scale = 8)
    private BigDecimal bidRate;

    @Column(precision = 19, scale = 8)
    private BigDecimal askRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExchangeRateLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "rate_source", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeRateSource rateSource;

    @Column(name = "provider_name", length = 100)
    @Enumerated(EnumType.STRING)
    private ExternalExchangeRateProvider providerName;

    @Column(name = "provider_reference_id", length = 100)
    private String providerReferenceId;

    @Column(name = "spread_percentage", precision = 8, scale = 4)
    private BigDecimal spreadPercentage;

    @Column(name = "is_manual_override", nullable = false)
    @Builder.Default
    private Boolean isManualOverride = false;

    @Column(name = "override_reason", length = 255)
    private String overrideReason;

    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public BigDecimal inverseRate() {
        return BigDecimal.ONE.divide(rate, 8, RoundingMode.HALF_UP);
    }
}