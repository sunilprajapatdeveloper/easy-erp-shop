package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    @Size(max = 100)
    private final String firstname;

    @Size(max = 100)
    private final String middleName;

    @NotBlank
    @Size(max = 100)
    private final String lastname;

    @NotBlank
    @Size(max = 100)
    private final String username;

    @Email
    @NotBlank
    @Size(max = 150)
    private final String email;

    @Size(max = 15)
    private final String phone;

    private final Boolean mfaEnabled;

    private String profileImageUrl;

    private final String addressLine1;

    private final String addressLine2;

    private final String city;

    private final String state;

    private final String country;

    private final String postalCode;

    private final String timezone;

    private final String language;

    private final String gender;

    @NotNull
    private final Long roleId;

    private final String department;

    private final String positionTitle;

    @NotNull
    private final Long companyId;

    /**
     * Branch/warehouse assignments for this user.
     * Can be one or multiple warehouse IDs.
     */
    private final Set<Long> warehouseIds;

    /**
     * Default warehouse for this user.
     * Must be included in warehouseIds if assigned.
     */
    private final Long defaultWarehouseId;
}
