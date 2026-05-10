package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale_return_product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleReturnProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_return_id", nullable = false)
    private SaleReturn saleReturn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal productUnitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "discount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discount;

    @Column(name = "tax_name", nullable = false, length = 100)
    private String taxName;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_category", nullable = false, length = 50)
    private TaxCategory taxCategory;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 3)
    private BigDecimal taxRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_inclusion_type", nullable = false, length = 50)
    private TaxInclusionType taxInclusionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_application_order", nullable = false, length = 50)
    private TaxApplicationOrder taxApplicationOrder;

    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "sub_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal subTotal;

    @Column(name = "created_by", updatable = false, nullable = false)
    private Long createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", updatable = false, nullable = false)
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