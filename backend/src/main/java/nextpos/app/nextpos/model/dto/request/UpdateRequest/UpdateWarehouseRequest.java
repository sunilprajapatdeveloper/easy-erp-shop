package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating an existing Warehouse.
 * Enterprise-level: allows partial updates and multi-tenant safety.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWarehouseRequest {

    @NotNull(message = "Warehouse ID is required")
    private Long id;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @Size(max = 150)
    private String name;

    @Size(max = 30)
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Size(max = 15)
    private String zipCode;

    /**
     * Mark if this warehouse is the default for the company
     */
    private Boolean isDefault;

    /**
     * Mark if this warehouse is the head office / HQ
     */
    private Boolean headquarter;

    /**
     * Each warehouse operates in one currency
     */
    private Long currencyId;

    /**
     * Timezone of the warehouse (e.g., "Asia/Kolkata")
     */
    @Size(max = 50)
    private String timezone;

    private Boolean active;

    /**
     * Whether this warehouse applies GST/VAT/Service Tax locally
     */
    private Boolean applyTax;

    /**
     * Whether this warehouse applies TDS (withholding tax)
     */
    private Boolean applyTds;

    /**
     * Whether to track stock in this warehouse
     */
    private Boolean trackInventory;

    /**
     * Optional invoice prefix for generated invoices (e.g., "DELHI-INV")
     */
    @Size(max = 20)
    private String invoicePrefix;

    /**
     * User updating this warehouse (audit)
     */
    private Long updatedBy;
}
