package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private final Long id;

    @Size(max = 100)
    private final String firstname;

    @Size(max = 100)
    private final String middleName;

    @Size(max = 100)
    private final String lastname;

    @Size(max = 100)
    private final String username;

    @Email
    @Size(max = 150)
    private final String email;

    @Size(max = 15)
    private final String phone;

    private final Boolean mfaEnabled;

    private final String profileImageUrl;

    private final String addressLine1;

    private final String addressLine2;

    private final String city;

    private final String state;

    private final String country;

    private final String postalCode;

    private final String timezone;

    private final String language;

    private final String gender;

    private final Long roleId;

    private final String department;

    private final String positionTitle;

    private final Long companyId;

    private final Set<Long> warehouseIds;

    private final Long defaultWarehouseId;
}
