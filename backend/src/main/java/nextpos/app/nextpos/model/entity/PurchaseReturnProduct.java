package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_return_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseReturnProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_return_id", nullable = false)
    private PurchaseReturn purchaseReturn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "return_qty", nullable = false)
    private Integer returnQty;

    @Column(name = "product_unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal productUnitCost;

    @Column(name = "product_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal productTax;

    @Column(name = "product_discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal productDiscount;

    @Column(name = "sub_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal subTotal;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

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
