package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating an existing ProductStock entry.
 * Designed for large-scale enterprise applications:
 * - Validation ensures data consistency
 * - Supports granular stock-level updates
 * - Explicit typing for monetary and quantity fields
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductStockRequest {

    /**
     * Unique identifier of the ProductStock being updated.
     */
    @NotNull(message = "ProductStock ID is required")
    private Long id;

    /**
     * Identifier of the associated Product.
     * Required to ensure update consistency and multi-tenant integrity.
     */
    @NotNull(message = "Product ID is required")
    private Long productId;

    /**
     * Identifier of the associated Warehouse.
     * Required for multi-warehouse stock management.
     */
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    /**
     * Current available quantity of stock in the warehouse.
     */
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    /**
     * Reserved stock that cannot be sold.
     */
    @PositiveOrZero(message = "Reserved quantity must be zero or positive")
    private Integer reservedQuantity;

    /**
     * Stock currently in transit to the warehouse.
     */
    @PositiveOrZero(message = "In-transit quantity must be zero or positive")
    private Integer inTransitQuantity;

    /**
     * Committed stock allocated for orders but not yet fulfilled.
     */
    @PositiveOrZero(message = "Committed quantity must be zero or positive")
    private Integer committedQuantity;

    /**
     * Minimum stock level to maintain for safety.
     */
    @Min(value = 0, message = "Minimum stock level must be zero or positive")
    private Integer minStockLevel;

    /**
     * Maximum stock level allowed.
     */
    @Min(value = 0, message = "Maximum stock level must be zero or positive")
    private Integer maxStockLevel;

    /**
     * Reorder threshold level.
     */
    @Min(value = 0, message = "Reorder level must be zero or positive")
    private Integer reorderLevel;

    /**
     * Whether stock alert is triggered for this item.
     */
    private Boolean stockAlert;

    /**
     * Average cost per unit (precision aligned with DB schema: 18,4).
     */
    @DecimalMin(value = "0.0000", inclusive = true, message = "Average cost must be zero or positive")
    private BigDecimal averageCost;

    /**
     * Date of last physical stock count.
     */
    private LocalDate lastCountDate;

    /**
     * Scheduled date for next physical stock count.
     */
    private LocalDate nextCountDate;
}
