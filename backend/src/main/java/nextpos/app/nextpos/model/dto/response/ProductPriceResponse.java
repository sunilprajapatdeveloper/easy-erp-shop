package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.ProductPrice;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for ProductPrice entity.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceResponse {

    private Long id;

    private Long productId;
    private String productCode;
    private String productName;

    private Long warehouseId;
    private String warehouseName;

    private String priceList;
    private String channel;
    private String customerGroup;

    private BigDecimal price;
    private BigDecimal cost;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Long currencyId;
    private String currencyCode;
    private String currencySymbol;

    private Boolean isActive;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private Integer minQuantity;
    private Integer maxQuantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convert ProductPrice entity → ProductPriceResponse DTO.
     */
    public static ProductPriceResponse fromEntity(ProductPrice entity) {
        if (entity == null) {
            return null;
        }

        Product product = entity.getProduct();
        Warehouse warehouse = entity.getWarehouse();
        Currency currency = entity.getCurrency();

        return ProductPriceResponse.builder()
                .id(entity.getId())
                .productId(product != null ? product.getId() : null)
                .productCode(product != null ? product.getCode() : null)
                .productName(product != null ? product.getName() : null)
                .warehouseId(warehouse != null ? warehouse.getId() : null)
                .warehouseName(warehouse != null ? warehouse.getName() : null)
                .priceList(entity.getPriceList())
                .channel(entity.getChannel())
                .customerGroup(entity.getCustomerGroup())
                .price(entity.getPrice())
                .cost(entity.getCost())
                .minPrice(entity.getMinPrice())
                .maxPrice(entity.getMaxPrice())
                .currencyId(currency != null ? currency.getId() : null)
                .currencyCode(currency != null ? currency.getCode() : null)
                .currencySymbol(currency != null ? currency.getSymbol() : null)
                .isActive(entity.getIsActive())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .minQuantity(entity.getMinQuantity())
                .maxQuantity(entity.getMaxQuantity())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
