package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder

public class UpdatePasswordRequest {
    @NotBlank private final String currentPassword;
    @NotBlank private final String newPassword;
}
