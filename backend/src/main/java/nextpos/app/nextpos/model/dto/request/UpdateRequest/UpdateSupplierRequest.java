package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class UpdateSupplierRequest {

    @Size(max = 150, message = "Supplier name cannot exceed 150 characters")
    private final String name;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private final String email;

    @Size(max = 50, message = "Phone cannot exceed 50 characters")
    private final String phone;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private final String country;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private final String city;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private final String address;

    @Size(max = 100, message = "Tax number cannot exceed 100 characters")
    private final String taxNumber;
}
