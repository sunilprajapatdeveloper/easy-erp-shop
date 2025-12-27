package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

    @NotBlank
    @Size(max = 100)
    private final String firstname;

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

    @NotBlank
    @Size(min = 8, max = 255)
    private final String password;

    @Size(max = 15)
    private final String phone;
}