package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.DiscountScope;
import nextpos.app.nextpos.model.enums.DiscountSource;
import nextpos.app.nextpos.model.enums.DiscountType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts", indexes = {
        @Index(name = "idx_discount_company", columnList = "company_id"),
        @Index(name = "idx_discount_code", columnList = "code"),
        @Index(name = "idx_discount_active", columnList = "is_active"),
        @Index(name = "idx_discount_dates", columnList = "start_date, end_date"),
        @Index(name = "idx_discount_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_discount_scope", columnList = "scope")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 50)
    private String code;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 30)
    private DiscountScope scope;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 30)
    private DiscountSource source;

    @Column(name = "discount_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "max_discount_amount", precision = 15, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "min_order_amount", precision = 15, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(name = "max_order_amount", precision = 15, scale = 2)
    private BigDecimal maxOrderAmount;

    @Column(name = "is_stackable", nullable = false)
    @Builder.Default
    private Boolean stackable = false;

    @Column(name = "auto_apply", nullable = false)
    @Builder.Default
    private Boolean autoApply = false;

    @Column(name = "requires_manager_approval", nullable = false)
    @Builder.Default
    private Boolean requiresManagerApproval = false;

    @Column(name = "approval_required_above", precision = 15, scale = 2)
    private BigDecimal approvalRequiredAbove;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_limit_per_customer")
    private Integer usageLimitPerCustomer;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();

        if (updatedAt == null)
            updatedAt = LocalDateTime.now();

        if (isActive == null)
            isActive = true;

        if (stackable == null)
            stackable = false;

        if (autoApply == null)
            autoApply = false;

        if (requiresManagerApproval == null)
            requiresManagerApproval = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}