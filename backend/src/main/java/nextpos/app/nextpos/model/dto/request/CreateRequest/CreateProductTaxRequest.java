package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import nextpos.app.nextpos.model.enums.TaxType;

import java.math.BigDecimal;

/**
 * Request DTO for creating a ProductTax entry.
 */
@Getter
@AllArgsConstructor
@Builder
public class CreateProductTaxRequest {

    /** Associated product ID */
    @NotNull(message = "Product ID is required")
    private final Long productId;

    /** Optional warehouse ID. Null means global tax */
    private final Long warehouseId;

    /** Unique tax code */
    @NotBlank(message = "Tax code is required")
    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    private final String taxCode;

    /** Display name */
    @NotBlank(message = "Tax name is required")
    @Size(max = 100, message = "Tax name must not exceed 100 characters")
    private final String taxName;

    /** Tax type: PERCENTAGE, FIXED, etc. */
    @NotNull(message = "Tax type is required")
    private final TaxType taxType;

    /** Tax rate (percentage or fixed value depending on taxType) */
    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.000", inclusive = false, message = "Tax rate must be greater than zero")
    @Digits(integer = 2, fraction = 3, message = "Tax rate must have up to 2 digits and 3 decimals")
    private final BigDecimal taxRate;

    /** Inclusive tax (already included in price) */
    @Builder.Default
    private final Boolean isInclusive = false;

    /** Compound tax (applied on top of other taxes) */
    @Builder.Default
    private final Boolean isCompound = false;

    /** Active flag */
    @Builder.Default
    private final Boolean isActive = true;
}
