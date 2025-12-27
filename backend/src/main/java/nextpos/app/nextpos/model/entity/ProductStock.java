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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product_stocks", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "product_id", "warehouse_id" })
}, indexes = {
                @Index(name = "idx_product_stock_product", columnList = "product_id"),
                @Index(name = "idx_product_stock_warehouse", columnList = "warehouse_id"),
                @Index(name = "idx_product_stock_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProductStock extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "warehouse_id", nullable = false)
        private Warehouse warehouse;

        @Column(nullable = false)
        @Builder.Default
        private Integer quantity = 0;

        @Column(name = "reserved_quantity", nullable = false)
        @Builder.Default
        private Integer reservedQuantity = 0;

        @Column(name = "in_transit_quantity", nullable = false)
        @Builder.Default
        private Integer inTransitQuantity = 0;

        @Column(name = "committed_quantity", nullable = false)
        @Builder.Default
        private Integer committedQuantity = 0;

        @Column(name = "min_stock_level")       
        private Integer minStockLevel;

        @Column(name = "max_stock_level")
        private Integer maxStockLevel;

        @Column(name = "reorder_level")
        private Integer reorderLevel;

        @Column(name = "stock_alert", nullable = false)
        @Builder.Default
        private Boolean stockAlert = false;

        @Column(name = "average_cost", precision = 18, scale = 4)
        private BigDecimal averageCost;

        @Column(name = "last_count_date")
        private LocalDate lastCountDate;

        @Column(name = "next_count_date")
        private LocalDate nextCountDate;

        @Transient
        public Integer getAvailableQuantity() {
                return this.quantity - this.reservedQuantity - this.committedQuantity;
        }

        @PrePersist
        @PreUpdate
        protected void checkStockAlert() {
                if (this.minStockLevel != null && this.quantity <= this.minStockLevel) {
                        this.stockAlert = true;
                } else {
                        this.stockAlert = false;
                }
        }
}
