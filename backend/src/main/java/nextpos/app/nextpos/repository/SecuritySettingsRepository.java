package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.SecuritySettings;
import nextpos.app.nextpos.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecuritySettingsRepository extends JpaRepository<SecuritySettings, Long> {

    Optional<SecuritySettings> findByCompanyId(Long companyId);

    Optional<SecuritySettings> findByCompany(Company company);

    boolean existsByCompany(Company company);
}