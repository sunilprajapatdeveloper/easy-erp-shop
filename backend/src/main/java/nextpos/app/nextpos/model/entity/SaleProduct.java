package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import nextpos.app.nextpos.model.enums.TaxApplicationOrder;
import nextpos.app.nextpos.model.enums.TaxCategory;
import nextpos.app.nextpos.model.enums.TaxInclusionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal productUnitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "line_discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineDiscountAmount;

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

    @Column(name = "line_net_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineNetAmount;

    @Column(name = "line_tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineTaxAmount;

    @Column(name = "line_gross_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineGrossAmount;

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

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.lineDiscountAmount == null) {
            this.lineDiscountAmount = BigDecimal.ZERO;
        }

        if (this.lineNetAmount == null) {
            this.lineNetAmount = BigDecimal.ZERO;
        }

        if (this.lineTaxAmount == null) {
            this.lineTaxAmount = BigDecimal.ZERO;
        }

        if (this.lineGrossAmount == null) {
            this.lineGrossAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}