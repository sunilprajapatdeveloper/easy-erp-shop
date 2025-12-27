package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Supplier;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String address;
    private String taxNumber;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public SupplierResponse(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.email = supplier.getEmail();
        this.phone = supplier.getPhone();
        this.country = supplier.getCountry();
        this.city = supplier.getCity();
        this.address = supplier.getAddress();
        this.taxNumber = supplier.getTaxNumber();
        this.createdBy = supplier.getCreatedBy();
        this.createdAt = supplier.getCreatedAt();
        this.updatedBy = supplier.getUpdatedBy();
        this.updatedAt = supplier.getUpdatedAt();
        this.companyId = supplier.getCompanyId();
    }
}
