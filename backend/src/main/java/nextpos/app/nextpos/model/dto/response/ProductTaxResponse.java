package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import nextpos.app.nextpos.model.entity.ProductTax;
import nextpos.app.nextpos.model.enums.TaxCategory;

import java.math.BigDecimal;

/**
 * Response DTO for ProductTax entity.
 * Provides all relevant fields for API responses.
 */
@Getter
@AllArgsConstructor
@Builder
public class ProductTaxResponse {

    private final Long id;
    private final Long productId;
    private final Long warehouseId;
    private final String taxCode;
    private final String taxName;
    private final TaxCategory taxCategory;
    private final BigDecimal taxRate;
    private final Boolean isInclusive;
    private final Boolean isCompound;
    private final Boolean isActive;

    /**
     * Converts a ProductTax entity to a ProductTaxResponse DTO.
     *
     * @param tax the ProductTax entity
     * @return the response DTO
     */
    public static ProductTaxResponse fromEntity(ProductTax tax) {
        return ProductTaxResponse.builder()
                .id(tax.getId())
                .productId(tax.getProduct() != null ? tax.getProduct().getId() : null)
                .warehouseId(tax.getWarehouse() != null ? tax.getWarehouse().getId() : null)
                .taxCode(tax.getTaxCode())
                .taxName(tax.getTaxName())
                .taxCategory(tax.getTaxCategory())
                .taxRate(tax.getTaxRate())
                .isInclusive(tax.getIsInclusive())
                .isCompound(tax.getIsCompound())
                .isActive(tax.getIsActive())
                .build();
    }
}
