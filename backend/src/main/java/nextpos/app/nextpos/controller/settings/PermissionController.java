package nextpos.app.nextpos.controller.settings;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import nextpos.app.nextpos.model.dto.request.CreateRoleRequest;
import nextpos.app.nextpos.model.dto.response.RoleResponse;
import nextpos.app.nextpos.service.interf.GroupService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/permissions")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PermissionController {

    private final GroupService groupService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createRole(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getRoleById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(groupService.getAllRoles());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id,
            @Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.ok(groupService.updateRole(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        groupService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
