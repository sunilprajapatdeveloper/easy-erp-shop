package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Permission;

@Getter
@AllArgsConstructor
@Builder
public class PermissionResponse {
    private Long id;
    private String name;

    public static PermissionResponse fromEntity(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName().name());
    }
}
