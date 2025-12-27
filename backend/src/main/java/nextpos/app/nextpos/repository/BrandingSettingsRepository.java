package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.BrandingSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandingSettingsRepository extends JpaRepository<BrandingSettings, Long> {

    Optional<BrandingSettings> findByCompanyId(Long companyId);

    Optional<BrandingSettings> findByIdAndCompanyId(Long id, Long companyId);

    List<BrandingSettings> findAllByCompanyId(Long companyId);
}