package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.TenantAiSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TenantAiSettingsRepository extends JpaRepository<TenantAiSettings, Long> {
    Optional<TenantAiSettings> findByTenantId(String tenantId);

    @Modifying
    @Transactional
    @Query("UPDATE TenantAiSettings t SET t.tokensUsedCurrentMonth = t.tokensUsedCurrentMonth + :tokens " +
            "WHERE t.tenantId = :tenantId AND t.tokensUsedCurrentMonth + :tokens <= t.maxTokensPerMonth")
    int incrementTokens(@Param("tenantId") String tenantId, @Param("tokens") long tokens);
}