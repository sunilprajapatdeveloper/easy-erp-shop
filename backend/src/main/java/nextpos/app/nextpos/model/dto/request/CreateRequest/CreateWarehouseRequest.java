package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new Warehouse.
 * Enterprise-level: supports multi-company, multi-currency,
 * inventory tracking, tax rules, and invoice customization.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWarehouseRequest {

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 30, message = "Phone number must be at most 30 characters")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100)
    private String country;

    @Size(max = 15)
    private String zipCode;

    /**
     * Mark if this warehouse is the default for the company
     */
    @Builder.Default
    private Boolean isDefault = false;

    /**
     * Mark if this warehouse is the head office / HQ
     */
    @Builder.Default
    private Boolean headquarter = false;

    /**
     * Each warehouse operates in one currency.
     */
    @NotNull(message = "Currency ID is required")
    private Long currencyId;

    /**
     * Timezone of the warehouse (e.g., "Asia/Kolkata")
     */
    @NotBlank(message = "Timezone is required")
    @Size(max = 50)
    @Builder.Default
    private String timezone = "UTC";

    @Builder.Default
    private Boolean active = true;

    /**
     * Whether this warehouse applies GST/VAT/Service Tax locally
     */
    @Builder.Default
    private Boolean applyTax = true;

    /**
     * Whether this warehouse applies TDS (withholding tax)
     */
    @Builder.Default
    private Boolean applyTds = false;

    /**
     * Whether to track stock in this warehouse
     */
    @Builder.Default
    private Boolean trackInventory = true;

    /**
     * Optional invoice prefix for generated invoices (e.g., "DELHI-INV")
     */
    @Size(max = 20)
    private String invoicePrefix;

    /**
     * Owning company (multi-tenant context)
     */
    @NotNull(message = "Company ID is required")
    private Long companyId;

    /**
     * User creating this warehouse (audit)
     */
    private Long createdBy;
}
