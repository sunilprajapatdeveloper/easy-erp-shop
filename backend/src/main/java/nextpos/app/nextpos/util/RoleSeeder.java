package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Role;
import nextpos.app.nextpos.model.entity.Permission;
import nextpos.app.nextpos.model.enums.UserRole;
import nextpos.app.nextpos.repository.RoleRepository;
import nextpos.app.nextpos.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Seed only the COMPANY_OWNER role
        String roleName = UserRole.COMPANY_OWNER.name();

        if (roleRepository.findByName(roleName).isEmpty()) {
            List<Permission> allPermissions = permissionRepository.findAll();

            if (allPermissions.isEmpty()) {
                System.err.println("Warning: No permissions found. Make sure PermissionSeeder runs before RoleSeeder.");
                return; // Skip seeding if permissions aren't available
            }

            Role companyOwnerRole = Role.builder()
                    .name(roleName)
                    .description("Company Owner with full access")
                    .companyId(defaultCompanyId)
                    .createdBy(defaultCreatedBy)
                    .createdAt(now)
                    .permissions(new HashSet<>(allPermissions))
                    .build();

            roleRepository.save(companyOwnerRole);
            System.out.println("COMPANY_OWNER role created with " + allPermissions.size() + " permissions.");
        }
    }
}
