package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_usages", indexes = {
        @Index(name = "idx_promotion_usage_promotion", columnList = "promotion_id"),
        @Index(name = "idx_promotion_usage_customer", columnList = "customer_id"),
        @Index(name = "idx_promotion_usage_sale", columnList = "sale_id"),
        @Index(name = "idx_promotion_usage_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PromotionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @Column(name = "usage_count", nullable = false)
    @Builder.Default
    private Integer usageCount = 1;

    @CreatedDate
    @Column(name = "used_at", nullable = false, updatable = false)
    private LocalDateTime usedAt;
}