package nextpos.app.nextpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

import nextpos.app.nextpos.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Optional<Role> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Role> findByNameIgnoreCaseAndCompanyId(String name, Long companyId);

    List<Role> findAllByCompanyId(Long companyId);
}
