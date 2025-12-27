package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Permission;
import nextpos.app.nextpos.model.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(PermissionType name);
}