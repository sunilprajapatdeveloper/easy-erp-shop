package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ProductPrice
 *
 * Defines contextual pricing for a product.
 * Supports warehouse-specific prices, price lists, sales channels,
 * customer groups, quantity breaks, validity dates, and multi-currency.
 */
@Entity
@Table(name = "product_prices", uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                                "product_id",
                                "warehouse_id",
                                "price_list",
                                "channel",
                                "customer_group",
                                "company_id"
                })
}, indexes = {
                @Index(name = "idx_product_price_product", columnList = "product_id"),
                @Index(name = "idx_product_price_warehouse", columnList = "warehouse_id"),
                @Index(name = "idx_product_price_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProductPrice extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        /** Associated product */
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        /** Optional: warehouse-specific pricing (null = global/company-wide) */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "warehouse_id")
        private Warehouse warehouse;

        /** Price list name: e.g. DEFAULT, WHOLESALE, SEASONAL */
        @Column(name = "price_list", nullable = false, length = 50)
        @Builder.Default
        private String priceList = "DEFAULT";

        /** Sales channel: POS, ONLINE, MOBILE, etc. */
        @Column(name = "channel", length = 50)
        private String channel;

        /** Customer group: e.g. RETAIL, WHOLESALE, VIP */
        @Column(name = "customer_group", length = 50)
        private String customerGroup;

        /** Selling price */
        @Column(nullable = false, precision = 18, scale = 4)
        private BigDecimal price;

        /** Cost reference (may differ from Product's default cost) */
        @Column(name = "cost", precision = 18, scale = 4)
        private BigDecimal cost;

        /** Minimum allowed price (floor) */
        @Column(name = "min_price", precision = 18, scale = 4)
        private BigDecimal minPrice;

        /** Maximum allowed price (ceiling) */
        @Column(name = "max_price", precision = 18, scale = 4)
        private BigDecimal maxPrice;

        /** Currency of this price */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "currency_id", nullable = false)
        private Currency currency;

        /** Active flag */
        @Column(name = "is_active", nullable = false)
        @Builder.Default
        private Boolean isActive = true;

        /** Validity window for this price (optional) */
        @Column(name = "valid_from")
        private LocalDateTime validFrom;

        @Column(name = "valid_to")
        private LocalDateTime validTo;

        /** Quantity-based pricing (min and max range) */
        @Column(name = "min_quantity", nullable = false)
        @Builder.Default
        private Integer minQuantity = 1;

        @Column(name = "max_quantity")
        private Integer maxQuantity;

        /** Defaulting rules */
        @PrePersist
        protected void onCreate() {
                super.onCreate();
                if (this.priceList == null) {
                        this.priceList = "DEFAULT";
                }
                if (this.minQuantity == null) {
                        this.minQuantity = 1;
                }
                if (this.isActive == null) {
                        this.isActive = true;
                }
        }
}
