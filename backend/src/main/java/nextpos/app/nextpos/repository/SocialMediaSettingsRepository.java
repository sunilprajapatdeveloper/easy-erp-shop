package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.SocialMediaSettings;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.enums.SocialMediaPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialMediaSettingsRepository extends JpaRepository<SocialMediaSettings, Long> {

    Optional<SocialMediaSettings> findByCompanyId(Long companyId);

    Optional<SocialMediaSettings> findByCompanyAndPlatform(Company company, SocialMediaPlatform platform);

    List<SocialMediaSettings> findAllByCompany(Company company);
}
