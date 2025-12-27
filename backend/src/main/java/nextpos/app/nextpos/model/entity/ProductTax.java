package nextpos.app.nextpos.model.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nextpos.app.nextpos.model.enums.TaxType;

import java.math.BigDecimal;

@Entity
@Table(name = "product_taxes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "product_id", "warehouse_id", "tax_code" })
}, indexes = {
        @Index(name = "idx_product_tax_product", columnList = "product_id"),
        @Index(name = "idx_product_tax_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_product_tax_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProductTax extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /** Associated product */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Optional warehouse-specific tax override (null = global/company-wide) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    /** Unique tax code (within company/warehouse context) */
    @Column(name = "tax_code", nullable = false, length = 20)
    private String taxCode;

    /** Display name of the tax */
    @Column(name = "tax_name", nullable = false, length = 100)
    private String taxName;

    /** Tax type: PERCENTAGE, FIXED, etc. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_type", nullable = false, length = 20)
    private TaxType taxType;

    /** Tax rate (percentage or fixed amount depending on taxType) */
    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 3)
    private BigDecimal taxRate;

    /** Inclusive tax (already included in price) */
    @Column(name = "is_inclusive", nullable = false)
    @Builder.Default
    private Boolean isInclusive = false;

    /** Compound tax (applied on top of other taxes) */
    @Column(name = "is_compound", nullable = false)
    @Builder.Default
    private Boolean isCompound = false;

    /** Active flag */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
