package nextpos.app.nextpos.model.dto.request;

import lombok.Data;
import nextpos.app.nextpos.model.enums.VerificationType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class VerificationValidationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String token;

    @NotNull
    private VerificationType verificationType;
}