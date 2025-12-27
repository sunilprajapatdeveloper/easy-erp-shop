package nextpos.app.nextpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import nextpos.app.nextpos.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}