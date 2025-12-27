package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.LoyaltySettings;
import nextpos.app.nextpos.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoyaltySettingsRepository extends JpaRepository<LoyaltySettings, Long> {

    Optional<LoyaltySettings> findByCompanyId(Long companyId);

    Optional<LoyaltySettings> findByIdAndCompany(Long id, Company company);

    List<LoyaltySettings> findAllByCompany(Company company);
}
