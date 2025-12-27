package nextpos.app.nextpos.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.model.enums.PlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "subscription_plans", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "billing_cycle" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human-readable name of the plan (e.g., Basic, Pro, Enterprise). */
    @Column(nullable = false, length = 100)
    private String name;

    /** Detailed description of the plan and its offerings. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Base price of the plan per billing cycle. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /** Currency code in ISO format (e.g., USD, EUR, INR). */
    @Column(nullable = false, length = 10)
    private String currency;

    /** Defines billing cycle of the plan. */
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 20)
    private BillingCycle billingCycle;

    /** If this plan allows a trial period. */
    @Builder.Default
    @Column(nullable = false)
    private boolean trialAvailable = false;

    /** Trial period duration in days (if applicable). */
    @Builder.Default
    @Column(nullable = false)
    private int trialDays = 0;

    /** Maximum number of users allowed under this plan. Null/0 means unlimited. */
    @Column
    private Integer maxUsers;

    /** Maximum number of branches allowed. Null/0 means unlimited. */
    @Column
    private Integer maxBranches;

    /** Feature toggles stored as key-value pairs. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_plan_features", joinColumns = @JoinColumn(name = "plan_id"))
    @MapKeyColumn(name = "feature_key")
    @Column(name = "feature_value")
    private Map<String, String> features;

    /** List of regions/countries where this plan is available. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_plan_regions", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "region")
    private List<String> availableRegions;

    /** Plan status (active/inactive). */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private PlanStatus status = PlanStatus.ACTIVE;

    /** Soft delete flag. */
    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

    /** Auditing fields. */
    @Column(nullable = false, updatable = false)
    private Long createdBy;

    @Column
    private Long updatedBy;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /** Optimistic locking for concurrency control. */
    @Version
    private Long version;
}
