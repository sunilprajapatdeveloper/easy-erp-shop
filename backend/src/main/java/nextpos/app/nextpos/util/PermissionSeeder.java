package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Permission;
import nextpos.app.nextpos.model.enums.PermissionType;
import nextpos.app.nextpos.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
@RequiredArgsConstructor
public class PermissionSeeder implements CommandLineRunner {

        private final PermissionRepository permissionRepository;

        @Override
        public void run(String... args) {
                Long defaultCompanyId = 1L; // Set your default company id here
                LocalDateTime now = LocalDateTime.now();

                for (PermissionType type : PermissionType.values()) {
                        permissionRepository.findByName(type)
                                        .orElseGet(() -> permissionRepository.save(
                                                        Permission.builder()
                                                                        .name(type)
                                                                        .companyId(defaultCompanyId)
                                                                        .createdAt(now)
                                                                        .build()));
                }
        }
}
