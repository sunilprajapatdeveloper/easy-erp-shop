package nextpos.app.nextpos.model.dto.request.CreateRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.entity.ProductStatus;
import nextpos.app.nextpos.model.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO used when creating a new Product.
 * - Uses ID references for relationships (categoryId, brandId, unit ids).
 * - Accepts imageUrls as a JSON array (List<String>) which will be serialized
 * by the mapper.
 *
 * Validation aims to match product entity constraints (lengths, required
 * fields).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;

    @NotBlank(message = "Product code is required")
    @Size(max = 100, message = "Code must be at most 100 characters")
    private String code;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must be at most 100 characters")
    private String sku;

    @Size(max = 150, message = "Barcode must be at most 150 characters")
    private String barcode;

    @NotNull(message = "Category ID is required")
    @JsonProperty("categoryId")
    private Long categoryId;

    /**
     * Optional brand (may be null)
     */
    @JsonProperty("brandId")
    private Long brandId;

    /**
     * Product / inventory type. Defaults to STOCK if not provided by API client.
     */
    @Builder.Default
    private ProductType productType = ProductType.STOCK;

    /**
     * Status: ACTIVE/INACTIVE. Defaults to ACTIVE.
     */
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;

    /**
     * Required base unit id (product unit)
     */
    @NotNull(message = "productUnitId is required")
    @JsonProperty("productUnitId")
    private Long productUnitId;

    /**
     * Optional sales unit id
     */
    @JsonProperty("salesUnitId")
    private Long salesUnitId;

    /**
     * Optional purchase unit id
     */
    @JsonProperty("purchaseUnitId")
    private Long purchaseUnitId;

    /**
     * Conversion factor (purchase/sales unit -> base unit). Must be >= 0 if
     * present.
     * Stored with precision up to scale=4 in DB.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "unitConversionFactor must be >= 0")
    @Digits(integer = 10, fraction = 4, message = "unitConversionFactor max precision is 10, scale is 4")
    private BigDecimal unitConversionFactor;

    @NotNull(message = "isBatchManaged must be specified (true or false)")
    @Builder.Default
    private Boolean isBatchManaged = false;

    @NotNull(message = "isSerialized must be specified (true or false)")
    @Builder.Default
    private Boolean isSerialized = false;

    @NotNull(message = "isComposite must be specified (true or false)")
    @Builder.Default
    private Boolean isComposite = false;

    @NotNull(message = "hasVariants must be specified (true or false)")
    @Builder.Default
    private Boolean hasVariants = false;

    /**
     * Physical attributes (optional)
     */
    @Digits(integer = 10, fraction = 3, message = "weight max precision is 10, scale is 3")
    private BigDecimal weight;

    @Digits(integer = 10, fraction = 3, message = "volume max precision is 10, scale is 3")
    private BigDecimal volume;

    @Size(max = 50, message = "dimensions must be at most 50 characters")
    private String dimensions;

    @Size(max = 2000, message = "description must be at most 2000 characters")
    private String description;

    /**
     * Primary image url string (optional)
     */
    @Size(max = 1000, message = "productImage URL too long")
    private String productImage;

    /**
     * Multiple images. Prefer JSON array in request body. Mapper will serialize
     * this into DB LOB.
     */
    private List<@Size(max = 1000, message = "each image URL must be <= 1000 chars") String> imageUrls;

    /**
     * Optional soft-delete flag. Normally controllers will not accept this for
     * create,
     * but included for completeness if caller wants to set a different default.
     */
    @Builder.Default
    private Boolean isDeleted = false;
}
