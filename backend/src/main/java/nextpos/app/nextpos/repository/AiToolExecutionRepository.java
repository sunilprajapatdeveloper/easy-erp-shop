package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.AiToolExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AiToolExecutionRepository extends JpaRepository<AiToolExecution, Long> {
    List<AiToolExecution> findByRequestId(UUID requestId);
}