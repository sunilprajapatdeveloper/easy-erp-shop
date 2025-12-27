package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new ProductStock entry.
 * Optimized for large-scale multi-warehouse systems.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductStockRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    /**
     * Initial stock quantity. Defaults to 0 if not provided.
     */
    @Min(value = 0, message = "Quantity must be non-negative")
    @Builder.Default
    private Integer quantity = 0;

    /**
     * Reserved quantity for pending orders, default 0.
     */
    @Min(value = 0, message = "Reserved quantity must be non-negative")
    @Builder.Default
    private Integer reservedQuantity = 0;

    /**
     * In-transit quantity (stock on the way), default 0.
     */
    @Min(value = 0, message = "In-transit quantity must be non-negative")
    @Builder.Default
    private Integer inTransitQuantity = 0;

    /**
     * Committed quantity (allocated to confirmed orders), default 0.
     */
    @Min(value = 0, message = "Committed quantity must be non-negative")
    @Builder.Default
    private Integer committedQuantity = 0;

    /**
     * Minimum stock threshold to trigger stock alert.
     */
    @Min(value = 0, message = "Minimum stock level must be non-negative")
    private Integer minStockLevel;

    /**
     * Maximum stock threshold (for overstock alerts).
     */
    @Min(value = 0, message = "Maximum stock level must be non-negative")
    private Integer maxStockLevel;

    /**
     * Reorder level (when to trigger replenishment).
     */
    @Min(value = 0, message = "Reorder level must be non-negative")
    private Integer reorderLevel;

    /**
     * Average cost of stock (weighted moving average).
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Average cost must be non-negative")
    private BigDecimal averageCost;

    /**
     * Last physical stock count date.
     */
    private LocalDate lastCountDate;

    /**
     * Next scheduled stock count date.
     */
    private LocalDate nextCountDate;
}
