package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.enums.TaxApplicationOrder;
import nextpos.app.nextpos.model.enums.TaxCategory;
import nextpos.app.nextpos.model.enums.TaxInclusionType;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing ProductTax entry.
 * All fields are optional to support partial updates.
 */
@Getter
@AllArgsConstructor
@Builder
public class UpdateProductTaxRequest {

    /** Optional: change associated product */
    private final Long productId;

    /** Optional: change warehouse (null = global) */
    private final Long warehouseId;

    /** Optional: unique tax code (max 20 chars) */
    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    private final String taxCode;

    /** Optional: display name (max 100 chars) */
    @Size(max = 100, message = "Tax name must not exceed 100 characters")
    private final String taxName;

    /** Optional: tax category */
    private final TaxCategory taxCategory;

    /** Optional: tax rate */
    @DecimalMin(value = "0.000", inclusive = false, message = "Tax rate must be greater than zero")
    @Digits(integer = 2, fraction = 3, message = "Tax rate must have up to 2 digits and 3 decimals")
    private final BigDecimal taxRate;

    /** Optional override for tax inclusion strategy. */
    private final TaxInclusionType overrideInclusionType;

    /** Optional override for tax application order. */
    private final TaxApplicationOrder overrideApplicationOrder;

    /** Optional: compound tax */
    private final Boolean isCompound;

    /** Optional: active flag */
    private final Boolean isActive;
}