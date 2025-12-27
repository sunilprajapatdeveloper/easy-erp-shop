package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.entity.Warehouse;

import java.time.LocalDateTime;

/**
 * Response DTO for Warehouse entity.
 * Provides a full enterprise-level view of warehouse details.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    private boolean headquarter;

    private Long currencyId;
    private String timezone;
    private boolean active;

    private boolean applyTax;
    private boolean applyTds;
    private boolean trackInventory;

    private String invoicePrefix;

    private boolean isDefault;
    private boolean isDeleted;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;

    private Long companyId;

    /**
     * Constructor to map from entity to response DTO.
     */
    public WarehouseResponse(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.phone = warehouse.getPhone();
        this.email = warehouse.getEmail();
        this.addressLine1 = warehouse.getAddressLine1();
        this.addressLine2 = warehouse.getAddressLine2();
        this.city = warehouse.getCity();
        this.state = warehouse.getState();
        this.country = warehouse.getCountry();
        this.zipCode = warehouse.getZipCode();
        this.headquarter = warehouse.isHeadquarter();
        this.currencyId = warehouse.getCurrency() != null ? warehouse.getCurrency().getId() : null;
        this.timezone = warehouse.getTimezone();
        this.active = warehouse.isActive();
        this.applyTax = warehouse.isApplyTax();
        this.applyTds = warehouse.isApplyTds();
        this.trackInventory = warehouse.isTrackInventory();
        this.invoicePrefix = warehouse.getInvoicePrefix();
        this.isDefault = warehouse.isDefault();
        this.isDeleted = warehouse.isDeleted();
        this.createdBy = warehouse.getCreatedBy();
        this.createdAt = warehouse.getCreatedAt();
        this.updatedBy = warehouse.getUpdatedBy();
        this.updatedAt = warehouse.getUpdatedAt();
        this.companyId = warehouse.getCompanyId();
    }
}
