package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.AiPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AiPromptRepository extends JpaRepository<AiPrompt, Long> {
    Optional<AiPrompt> findFirstByPromptKeyAndIsActiveTrueOrderByVersionDesc(String promptKey);

    List<AiPrompt> findAllByPromptKeyOrderByVersionDesc(String promptKey);
}