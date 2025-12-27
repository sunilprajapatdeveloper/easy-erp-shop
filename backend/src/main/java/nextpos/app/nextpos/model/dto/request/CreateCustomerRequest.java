package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    @NotBlank private final String name;
    @Email private final String email;
    private final String phone;
    private final String country;
    private final String city;
}
