package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.SMTPSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SMTPSettingsRepository extends JpaRepository<SMTPSettings, Long> {

    /**
     * Find active SMTP settings for a specific company
     * Each company can have only one SMTP configuration
     */
    Optional<SMTPSettings> findByCompanyIdAndIsActiveTrue(Long companyId);

    /**
     * Check if a company already has SMTP settings configured
     */
    boolean existsByCompanyId(Long companyId);

    /**
     * Soft delete by deactivating
     */
    @Modifying
    @Transactional
    @Query("UPDATE SMTPSettings s SET s.isActive = false WHERE s.company.id = :companyId")
    void deactivateByCompanyId(@Param("companyId") Long companyId);

    /**
     * Find by company ID regardless of active status
     */
    Optional<SMTPSettings> findByCompanyId(Long companyId);
}