package nextpos.app.nextpos.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfer_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal productUnitCost;

    @Column(name = "product_stock", nullable = false)
    private Integer productStock;

    @Column(name = "transferred_qty", nullable = false)
    private Integer transferredQty;

    @Column(name = "product_discount", precision = 10, scale = 2)
    private BigDecimal productDiscount;

    @Column(name = "product_tax", precision = 10, scale = 2)
    private BigDecimal productTax;

    @Column(name = "sub_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal subTotal;

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
