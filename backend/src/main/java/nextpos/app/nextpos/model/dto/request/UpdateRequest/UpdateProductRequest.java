package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
 * DTO for updating an existing Product.
 * - Product ID is mandatory.
 * - All other fields are optional. Only provided fields will be updated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProductRequest {

    @NotNull(message = "Product ID is required")
    private Long id;

    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;

    @Size(max = 100, message = "Code must be at most 100 characters")
    private String code;

    @Size(max = 100, message = "SKU must be at most 100 characters")
    private String sku;

    @Size(max = 150, message = "Barcode must be at most 150 characters")
    private String barcode;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("brandId")
    private Long brandId;

    private ProductType productType;

    private ProductStatus status;

    @JsonProperty("productUnitId")
    private Long productUnitId;

    @JsonProperty("salesUnitId")
    private Long salesUnitId;

    @JsonProperty("purchaseUnitId")
    private Long purchaseUnitId;

    @DecimalMin(value = "0.0", inclusive = true, message = "unitConversionFactor must be >= 0")
    @Digits(integer = 10, fraction = 4, message = "unitConversionFactor max precision is 10, scale is 4")
    private BigDecimal unitConversionFactor;

    private Boolean isBatchManaged;

    private Boolean isSerialized;

    private Boolean isComposite;

    private Boolean hasVariants;

    @Digits(integer = 10, fraction = 3, message = "weight max precision is 10, scale is 3")
    private BigDecimal weight;

    @Digits(integer = 10, fraction = 3, message = "volume max precision is 10, scale is 3")
    private BigDecimal volume;

    @Size(max = 50, message = "dimensions must be at most 50 characters")
    private String dimensions;

    @Size(max = 2000, message = "description must be at most 2000 characters")
    private String description;

    @Size(max = 1000, message = "productImage URL too long")
    private String productImage;

    private List<@Size(max = 1000, message = "each image URL must be <= 1000 chars") String> imageUrls;

    private Boolean isDeleted;
}
