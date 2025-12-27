package nextpos.app.nextpos.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductStatus;
import nextpos.app.nextpos.model.enums.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private Long id;

    /** Basic product info */
    private String name;
    private String code;
    private String sku;
    private String barcode;

    /** Category info */
    private Long categoryId;
    private String categoryName;

    /** Brand info */
    private Long brandId;
    private String brandName;

    /** Product type and status */
    private ProductType productType;
    private ProductStatus status;

    /** Unit info */
    private Long productUnitId;
    private String productUnitName;

    private Long salesUnitId;
    private String salesUnitName;

    private Long purchaseUnitId;
    private String purchaseUnitName;

    /** Conversion factor */
    private BigDecimal unitConversionFactor;

    /** Flags */
    private Boolean isBatchManaged;
    private Boolean isSerialized;
    private Boolean isComposite;
    private Boolean hasVariants;

    /** Physical attributes */
    private BigDecimal weight;
    private BigDecimal volume;
    private String dimensions;

    /** Content */
    private String description;
    private String productImage;
    private List<String> imageUrls;
    private List<MediaResponse> mediaImages;

    // For multiple warehouses (no warehouseId provided)
    @Builder.Default
    private List<ProductPriceResponse> prices = Collections.emptyList();

    @Builder.Default
    private List<ProductStockResponse> stocks = Collections.emptyList();

    @Builder.Default
    private List<ProductTaxResponse> taxes = Collections.emptyList();

    // For single warehouse (when warehouseId provided)
    private ProductPriceResponse price;
    private ProductStockResponse stock;
    private ProductTaxResponse tax;

    /** System flags */
    private Boolean isDeleted;

    /** Audit fields from BaseEntity */
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;

    /**
     * Convert Product entity → ProductResponse DTO.
     */
    public static ProductResponse fromEntity(Product product, List<MediaResponse> mediaResponse) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .code(product.getCode())
                .sku(product.getSku())
                .barcode(product.getBarcode())

                // Category info
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)

                // Brand info
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)

                // Product type and status
                .productType(product.getProductType())
                .status(product.getStatus())

                // Units
                .productUnitId(product.getProductUnit() != null ? product.getProductUnit().getId() : null)
                .productUnitName(product.getProductUnit() != null ? product.getProductUnit().getName() : null)
                .salesUnitId(product.getSalesUnit() != null ? product.getSalesUnit().getId() : null)
                .salesUnitName(product.getSalesUnit() != null ? product.getSalesUnit().getName() : null)
                .purchaseUnitId(product.getPurchaseUnit() != null ? product.getPurchaseUnit().getId() : null)
                .purchaseUnitName(product.getPurchaseUnit() != null ? product.getPurchaseUnit().getName() : null)

                // Conversion & flags
                .unitConversionFactor(product.getUnitConversionFactor())
                .isBatchManaged(Boolean.TRUE.equals(product.getIsBatchManaged()))
                .isSerialized(Boolean.TRUE.equals(product.getIsSerialized()))
                .isComposite(Boolean.TRUE.equals(product.getIsComposite()))
                .hasVariants(Boolean.TRUE.equals(product.getHasVariants()))

                // Physical attributes
                .weight(product.getWeight())
                .volume(product.getVolume())
                .dimensions(product.getDimensions())

                // Description and images
                .description(product.getDescription())
                .productImage(product.getProductImage())
                .imageUrls(product.getImageUrls() != null && !product.getImageUrls().isEmpty()
                        ? Arrays.asList(product.getImageUrls().split(","))
                        : Collections.emptyList())
                .mediaImages(mediaResponse != null ? mediaResponse : Collections.emptyList())

                .prices(Collections.emptyList())
                .stocks(Collections.emptyList())
                .taxes(Collections.emptyList())

                // System flags
                .isDeleted(Boolean.TRUE.equals(product.getIsDeleted()))

                // Audit fields
                .createdBy(product.getCreatedBy())
                .createdAt(product.getCreatedAt())
                .updatedBy(product.getUpdatedBy())
                .updatedAt(product.getUpdatedAt())

                .build();
    }

    public void setSingleWarehousePrice(ProductPriceResponse price) {
        this.price = price;
    }

    public void setSingleWarehouseStock(ProductStockResponse stock) {
        this.stock = stock;
    }

    public void setSingleWarehouseTax(ProductTaxResponse tax) {
        this.tax = tax;
    }
}
