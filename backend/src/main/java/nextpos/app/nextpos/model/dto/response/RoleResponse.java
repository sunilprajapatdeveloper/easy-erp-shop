package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private Set<Long> permissionIds;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();

        this.permissionIds = role.getPermissions() == null || role.getPermissions().isEmpty()
                ? Set.of()
                : role.getPermissions().stream()
                        .map(p -> p.getId())
                        .collect(Collectors.toSet());

        this.createdBy = role.getCreatedBy();
        this.createdAt = role.getCreatedAt();
        this.updatedBy = role.getUpdatedBy();
        this.updatedAt = role.getUpdatedAt();
        this.companyId = role.getCompanyId();
    }
}