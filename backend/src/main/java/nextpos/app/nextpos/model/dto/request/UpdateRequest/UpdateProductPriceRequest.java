package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing ProductPrice entry.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductPriceRequest {

    /** ID of the ProductPrice to update */
    @NotNull(message = "ProductPrice ID is required")
    private Long id;

    /** Associated product ID (cannot be null) */
    @NotNull(message = "Product ID is required")
    private Long productId;

    /** Optional warehouse-specific price (null = global/company-wide) */
    private Long warehouseId;

    /** Price list name (DEFAULT, WHOLESALE, etc.) */
    @Size(max = 50, message = "Price list must be at most 50 characters")
    private String priceList;

    /** Sales channel (POS, ONLINE, etc.) */
    @Size(max = 50, message = "Channel must be at most 50 characters")
    private String channel;

    /** Customer group (RETAIL, WHOLESALE, VIP, etc.) */
    @Size(max = 50, message = "Customer group must be at most 50 characters")
    private String customerGroup;

    /** Selling price */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    /** Cost reference */
    @DecimalMin(value = "0.0", inclusive = true, message = "Cost must be non-negative")
    private BigDecimal cost;

    /** Minimum allowed price (floor) */
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum price must be non-negative")
    private BigDecimal minPrice;

    /** Maximum allowed price (ceiling) */
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum price must be non-negative")
    private BigDecimal maxPrice;

    /** Currency ID */
    @NotNull(message = "Currency ID is required")
    private Long currencyId;

    /** Active flag */
    private Boolean isActive;

    /** Validity window */
    @FutureOrPresent(message = "Valid from must be present or future date")
    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    /** Quantity-based pricing */
    @NotNull(message = "Minimum quantity is required")
    @Min(value = 1, message = "Minimum quantity must be at least 1")
    private Integer minQuantity;

    @Min(value = 1, message = "Maximum quantity must be at least 1")
    private Integer maxQuantity;
}
