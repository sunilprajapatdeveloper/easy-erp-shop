package nextpos.app.nextpos.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.StockEffect;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "adjustment_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdjustmentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // Lazy fetch to avoid loading adjustment unless needed
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adjustment_id")
    private Adjustment adjustment;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "current_qty")
    private Integer currentQty;

    @Column(name = "adjusted_qty")
    private Integer adjustedQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_effect", nullable = false)
    private StockEffect stockEffect;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false)
    private Long companyId;
}
