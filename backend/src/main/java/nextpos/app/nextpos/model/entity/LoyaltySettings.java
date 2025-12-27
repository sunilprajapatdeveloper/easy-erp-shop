package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Loyalty program configuration for a company.
 * Supports points-based, cashback, and tiered loyalty models.
 */
@Entity
@Table(name = "loyalty_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class LoyaltySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // Association to Company
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Basic settings
    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Column(name = "program_name", length = 150)
    private String programName;

    // Loyalty model (e.g., POINTS, CASHBACK, TIERED)
    @Column(name = "loyalty_type", length = 50, nullable = false)
    private String loyaltyType;

    // Points system
    @Column(name = "points_per_currency", precision = 19, scale = 4)
    private BigDecimal pointsPerCurrency; // e.g., 1 point per 100 INR

    @Column(name = "currency_per_point", precision = 19, scale = 4)
    private BigDecimal currencyPerPoint; // e.g., 1 INR per 10 points

    @Column(name = "points_expiry_days")
    private Integer pointsExpiryDays;

    // Cashback system
    @Column(name = "cashback_percentage", precision = 5, scale = 2)
    private BigDecimal cashbackPercentage; // e.g., 5% cashback

    @Column(name = "min_order_amount_for_cashback", precision = 19, scale = 4)
    private BigDecimal minOrderAmountForCashback;

    // Tiered system (stored as JSON for flexibility)
    @Column(name = "tier_rules", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tierRules;
    /**
     * Example tierRules JSON:
     * {
     * "Silver": { "minSpend": 5000, "pointsMultiplier": 1.2 },
     * "Gold": { "minSpend": 20000, "pointsMultiplier": 1.5 },
     * "Platinum": { "minSpend": 50000, "pointsMultiplier": 2.0 }
     * }
     */

    // Redemption rules
    @Column(name = "min_points_to_redeem")
    private Integer minPointsToRedeem;

    @Column(name = "max_discount_percentage", precision = 5, scale = 2)
    private BigDecimal maxDiscountPercentage; // e.g., loyalty discount capped at 20%

    // Extra settings as JSON (for future extensibility)
    @Column(name = "extra_settings", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> extraSettings;

    // Status flags
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by", nullable = false, updatable = false)
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
