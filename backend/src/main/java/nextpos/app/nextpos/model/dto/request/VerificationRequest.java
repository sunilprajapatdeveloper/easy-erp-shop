package nextpos.app.nextpos.model.dto.request;

import lombok.Data;
import nextpos.app.nextpos.model.enums.VerificationType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class VerificationRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private VerificationType verificationType;

    private String referenceId;

    private String referenceType;

    private String metadata;
}