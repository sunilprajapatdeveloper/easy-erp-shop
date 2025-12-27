package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRoleRequest;
import nextpos.app.nextpos.model.dto.response.RoleResponse;

import java.util.List;

public interface GroupService {
    RoleResponse createRole(CreateRoleRequest request);
    RoleResponse getRoleById(Long id);
    List<RoleResponse> getAllRoles();
    RoleResponse updateRole(Long id, CreateRoleRequest request);
    void deleteRole(Long id);
}