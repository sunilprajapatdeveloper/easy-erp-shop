package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.AiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface AiUsageLogRepository extends JpaRepository<AiUsageLog, Long> {
    List<AiUsageLog> findByTenantIdAndCreatedAtBetween(String tenantId, Instant start, Instant end);

    long countByTenantIdAndCreatedAtAfter(String tenantId, Instant start);
}