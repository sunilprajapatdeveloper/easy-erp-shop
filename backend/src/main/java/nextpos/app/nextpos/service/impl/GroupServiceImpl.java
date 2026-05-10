package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import nextpos.app.nextpos.service.interf.GroupService;
import nextpos.app.nextpos.repository.PermissionRepository;
import nextpos.app.nextpos.repository.RoleRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.model.dto.request.CreateRoleRequest;
import nextpos.app.nextpos.model.dto.response.RoleResponse;
import nextpos.app.nextpos.model.entity.Role;
import nextpos.app.nextpos.model.entity.Permission;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private static final String PROTECTED_ROLE = "COMPANY_OWNER";

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Role already in use: " + request.getName());
        }

        // Fetch permissions by ID
        Set<Permission> permissions = request.getPermissionIds().stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + id)))
                .collect(Collectors.toSet());

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(permissions)
                .createdBy(UserContext.getCurrentUserId())
                .companyId(UserContext.getCurrentCompanyId())
                .createdAt(java.time.LocalDateTime.now())
                .build();

        return new RoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (PROTECTED_ROLE.equalsIgnoreCase(role.getName())) {
            throw new RuntimeException("Group not found");
        }

        return new RoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .filter(role -> !PROTECTED_ROLE.equalsIgnoreCase(role.getName()))
                .map(RoleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse updateRole(Long id, CreateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (PROTECTED_ROLE.equalsIgnoreCase(role.getName())) {
            throw new RuntimeException("Group not found");
        }

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Set<Permission> permissions = request.getPermissionIds().stream()
                .map(pid -> permissionRepository.findById(pid)
                        .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + pid)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        role.setUpdatedAt(java.time.LocalDateTime.now());

        return new RoleResponse(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (PROTECTED_ROLE.equalsIgnoreCase(role.getName())) {
            throw new RuntimeException("Group not found");
        }

        roleRepository.deleteById(id);
    }
}