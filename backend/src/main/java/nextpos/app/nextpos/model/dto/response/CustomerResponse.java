package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import nextpos.app.nextpos.model.entity.Customer;
@Getter
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String country;
    private String city;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.country = customer.getCountry();
        this.city = customer.getCity();
        this.createdBy = customer.getCreatedBy();
        this.createdAt = customer.getCreatedAt();
        this.updatedBy = customer.getUpdatedBy();
        this.updatedAt = customer.getUpdatedAt();
        this.companyId = customer.getCompanyId();
    }
}