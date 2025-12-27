package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.OnlineOrderingSettings;
import nextpos.app.nextpos.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnlineOrderingSettingsRepository extends JpaRepository<OnlineOrderingSettings, Long> {

    Optional<OnlineOrderingSettings> findByCompanyId(Long companyId);

    Optional<OnlineOrderingSettings> findByCompanyAndIsDeletedFalse(Company company);

    boolean existsByCompanyAndIsDeletedFalse(Company company);
}