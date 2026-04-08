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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ProductType;

import java.math.BigDecimal;

/**
 * Product master entity.
 * 
 * This stores only the "identity and core attributes" of a product.
 * Prices, stock levels, and warehouse-specific data are handled by
 * ProductPrice and ProductStock entities.
 */
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "company_id", "sku" }),
        @UniqueConstraint(columnNames = { "company_id", "code" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /** Product Name */
    @Column(nullable = false, length = 150)
    private String name;

    /** Internal/short product code */
    @Column(nullable = false, length = 100)
    private String code;

    /** Stock Keeping Unit - unique within company */
    @Column(nullable = false, length = 100)
    private String sku;

    /** Barcode/UPC/EAN */
    @Column(length = 150)
    private String barcode;

    /** Category reference */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Brand reference (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    /** Product type: STOCK, SERVICE, GIFT_CARD, etc. */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 20)
    @Builder.Default
    private ProductType productType = ProductType.STOCK;

    /** Status: ACTIVE / INACTIVE */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;

    /** Default product unit (base measurement) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_unit_id", nullable = false)
    private ProductUnit productUnit;

    /** Sales unit (optional, may differ from base unit) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_unit_id")
    private ProductUnit salesUnit;

    /** Purchase unit (optional, may differ from base unit) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_unit_id")
    private ProductUnit purchaseUnit;

    /**
     * Conversion factor between purchase/sales units and base product unit.
     * e.g. 1 box = 12 pieces → factor = 12.
     */
    @Column(name = "unit_conversion_factor", precision = 10, scale = 4)
    private BigDecimal unitConversionFactor;

    /** Batch tracking enabled */
    @Column(name = "is_batch_managed", nullable = false)
    @Builder.Default
    private Boolean isBatchManaged = false;

    /** Serialization enabled (IMEI, serial numbers) */
    @Column(name = "is_serialized", nullable = false)
    @Builder.Default
    private Boolean isSerialized = false;

    /** Composite product (bundles, kits) */
    @Column(name = "is_composite", nullable = false)
    @Builder.Default
    private Boolean isComposite = false;

    /** Has variants (sizes, colors, etc.) */
    @Column(name = "has_variants", nullable = false)
    @Builder.Default
    private Boolean hasVariants = false;

    /** Physical attributes */
    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(name = "volume", precision = 10, scale = 3)
    private BigDecimal volume;

    /** LxWxH string format */
    @Column(name = "dimensions", length = 50)
    private String dimensions;

    /** Rich text description */
    @Column(length = 2000)
    private String description;

    /** Primary product image */
    @Column(name = "product_image")
    private String productImage;

    /** Multiple images (comma-separated URLs or JSON) */
    @Lob
    @Column(name = "image_urls")
    private String imageUrls;

    /** Soft delete flag */
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "search_vector", insertable = false, updatable = false)
    private String searchVector;

    /** Hook for defaulting enums and flags */
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (this.status == null) {
            this.status = ProductStatus.ACTIVE;
        }
        if (this.productType == null) {
            this.productType = ProductType.STOCK;
        }
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        if (this.isBatchManaged == null) {
            this.isBatchManaged = false;
        }
        if (this.isSerialized == null) {
            this.isSerialized = false;
        }
        if (this.isComposite == null) {
            this.isComposite = false;
        }
        if (this.hasVariants == null) {
            this.hasVariants = false;
        }
    }
}
