package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWarehouseRequest {

    @Size(max = 150)
    private String name;

    @Size(max = 30)
    private String phone;

    @Email
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

    private Boolean isDefault;
    private Boolean headquarter;
    private Long currencyId;
    @Size(max = 50)
    private String timezone;
    private Boolean active;
    private Boolean applyTax;
    private Boolean applyTds;
    private Boolean trackInventory;
    @Size(max = 20)
    private String invoicePrefix;
}
