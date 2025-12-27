package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class CreateRoleRequest {
    @NotBlank private final String name;
    private final String description;
    @NotNull @Size(min = 1) private final Set<Long> permissionIds;
}
