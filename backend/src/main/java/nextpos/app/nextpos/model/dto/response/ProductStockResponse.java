package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.ProductStock;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for ProductStock.
 * Designed for large-scale enterprise applications:
 * - Lightweight representation with productId and warehouseId
 * - Includes computed fields like availableQuantity
 * - Safe for API consumers (no lazy entity references)
 */
@Getter
@AllArgsConstructor
@Builder
public class ProductStockResponse {

    /**
     * Unique identifier of the ProductStock record.
     */
    private final Long id;

    /**
     * Associated Product ID.
     */
    private final Long productId;

    /**
     * Associated Warehouse ID.
     */
    private final Long warehouseId;

    /**
     * Current available quantity of stock in the warehouse.
     */
    private final Integer quantity;

    /**
     * Reserved stock that cannot be sold.
     */
    private final Integer reservedQuantity;

    /**
     * Stock currently in transit to the warehouse.
     */
    private final Integer inTransitQuantity;

    /**
     * Committed stock allocated for orders but not yet fulfilled.
     */
    private final Integer committedQuantity;

    /**
     * Minimum stock level to maintain for safety.
     */
    private final Integer minStockLevel;

    /**
     * Maximum stock level allowed.
     */
    private final Integer maxStockLevel;

    /**
     * Reorder threshold level.
     */
    private final Integer reorderLevel;

    /**
     * Whether stock alert is triggered for this item.
     */
    private final Boolean stockAlert;

    /**
     * Average cost per unit (precision aligned with DB schema: 18,4).
     */
    private final BigDecimal averageCost;

    /**
     * Date of last physical stock count.
     */
    private final LocalDate lastCountDate;

    /**
     * Scheduled date for next physical stock count.
     */
    private final LocalDate nextCountDate;

    /**
     * Derived field: Available quantity for sale
     * (quantity - reservedQuantity - committedQuantity).
     */
    private final Integer availableQuantity;

    /**
     * Convert ProductStock entity → ProductStockResponse DTO.
     */
    public static ProductStockResponse fromEntity(ProductStock stock) {
        if (stock == null) {
            return null;
        }

        return ProductStockResponse.builder()
                .id(stock.getId())
                .productId(stock.getProduct() != null ? stock.getProduct().getId() : null)
                .warehouseId(stock.getWarehouse() != null ? stock.getWarehouse().getId() : null)
                .quantity(stock.getQuantity())
                .reservedQuantity(stock.getReservedQuantity())
                .inTransitQuantity(stock.getInTransitQuantity())
                .committedQuantity(stock.getCommittedQuantity())
                .minStockLevel(stock.getMinStockLevel())
                .maxStockLevel(stock.getMaxStockLevel())
                .reorderLevel(stock.getReorderLevel())
                .stockAlert(stock.getStockAlert())
                .averageCost(stock.getAverageCost())
                .lastCountDate(stock.getLastCountDate())
                .nextCountDate(stock.getNextCountDate())
                .availableQuantity(stock.getAvailableQuantity())
                .build();
    }
}
