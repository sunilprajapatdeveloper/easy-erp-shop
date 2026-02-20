package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWarehouseRequest {

    @NotBlank(message = "Warehouse name is required")
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

    @Builder.Default
    private Boolean isDefault = false;

    @Builder.Default
    private Boolean headquarter = false;

    @NotNull(message = "Currency ID is required")
    private Long currencyId;

    @Size(max = 50)
    private String timezone;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean applyTax = true;

    @Builder.Default
    private Boolean applyTds = false;

    @Builder.Default
    private Boolean trackInventory = true;

    @Size(max = 20)
    private String invoicePrefix;
}