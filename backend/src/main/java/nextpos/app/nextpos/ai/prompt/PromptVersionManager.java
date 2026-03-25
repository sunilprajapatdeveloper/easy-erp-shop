package nextpos.app.nextpos.ai.prompt;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.AiPrompt;
import nextpos.app.nextpos.repository.AiPromptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptVersionManager {
    private final AiPromptRepository promptRepository;

    @Transactional
    public AiPrompt createNewVersion(String promptKey, String template, String description, String createdBy) {
        // Deactivate current active version
        promptRepository.findFirstByPromptKeyAndIsActiveTrueOrderByVersionDesc(promptKey)
                .ifPresent(active -> {
                    active.setIsActive(false);
                    promptRepository.save(active);
                });
        // Determine next version number
        int maxVersion = promptRepository.findAllByPromptKeyOrderByVersionDesc(promptKey).stream()
                .findFirst().map(AiPrompt::getVersion).orElse(0);
        AiPrompt newPrompt = new AiPrompt();
        newPrompt.setPromptKey(promptKey);
        newPrompt.setVersion(maxVersion + 1);
        newPrompt.setPromptTemplate(template);
        newPrompt.setDescription(description);
        newPrompt.setCreatedBy(createdBy);
        newPrompt.setIsActive(true);
        return promptRepository.save(newPrompt);
    }

    public List<AiPrompt> listVersions(String promptKey) {
        return promptRepository.findAllByPromptKeyOrderByVersionDesc(promptKey);
    }
}