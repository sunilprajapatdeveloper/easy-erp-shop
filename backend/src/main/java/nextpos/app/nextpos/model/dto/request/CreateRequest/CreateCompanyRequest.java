package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 5, max = 20, message = "Phone number length must be between 5 and 20")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String registrationNumber;

    private String country;
    private String state;
    private String city;
    private String address;

    private String postalCode;
    private String timezone;

    /**
     * Lightweight feature toggles that the creation flow may seed.
     * The service will interpret these and create minimal settings objects if
     * requested.
     */
    private Boolean enableOnlineOrdering;
    private Boolean enableLoyaltyProgram;

    @NotBlank(message = "Created by user ID is required")
    private Long createdBy;
}
