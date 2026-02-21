package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    @Size(max = 100)
    private String firstname;

    @NotBlank
    @Size(max = 100)
    private String lastname;

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 15)
    private String phone;

    private Boolean mfaEnabled;
    private String profileImageUrl;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String timezone;
    private String language;
    private String gender;

    @NotNull
    private Long roleId;

    private String department;
    private String positionTitle;

    private Set<Long> warehouseIds;
    private Long defaultWarehouseId;
}
