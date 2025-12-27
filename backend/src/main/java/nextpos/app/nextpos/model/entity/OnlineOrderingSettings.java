package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "online_ordering_settings", uniqueConstraints = @UniqueConstraint(columnNames = "company_id"))
public class OnlineOrderingSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Each company can have only one OnlineOrderingSettings (per tenant).
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /**
     * Master toggle for enabling/disabling online ordering.
     */
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = false;

    /**
     * Custom domain or subdomain for online ordering portal.
     * Example: orders.mycompany.com
     */
    @Column(name = "ordering_url", length = 255)
    private String orderingUrl;

    /**
     * Minimum order value required for checkout.
     */
    @Column(name = "min_order_value", precision = 12, scale = 2)
    private BigDecimal  minOrderValue;

    /**
     * Estimated delivery time in minutes.
     */
    @Column(name = "estimated_delivery_time")
    private Integer estimatedDeliveryTime;

    /**
     * Toggle for self-pickup option.
     */
    @Column(name = "self_pickup_enabled", nullable = false)
    @Builder.Default
    private boolean selfPickupEnabled = true;

    /**
     * Toggle for delivery option.
     */
    @Column(name = "delivery_enabled", nullable = false)
    @Builder.Default
    private boolean deliveryEnabled = true;

    /**
     * Third-party integration key (aggregators).
     */
    @Column(name = "integration_key", length = 255)
    private String integrationKey;

    /**
     * Custom notes visible to customers.
     */
    @Column(name = "customer_notes", length = 2000)
    private String customerNotes;

    /**
     * JSON config for aggregator integrations.
     * Example: { "swiggy": { "apiKey": "...", "enabled": true } }
     */
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> integrationConfig;

    /**
     * Soft delete flag (to allow history / restore).
     */
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    /**
     * Audit fields.
     */
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
