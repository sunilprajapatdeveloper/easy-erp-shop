package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

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
}