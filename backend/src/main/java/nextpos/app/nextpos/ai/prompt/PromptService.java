package nextpos.app.nextpos.ai.prompt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.exception.AiException;
import nextpos.app.nextpos.model.entity.AiPrompt;
import nextpos.app.nextpos.repository.AiPromptRepository;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {
    private final AiPromptRepository promptRepository;
    private final MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    public String getRenderedPrompt(String promptKey, Map<String, Object> variables) {
        AiPrompt prompt = promptRepository.findFirstByPromptKeyAndIsActiveTrueOrderByVersionDesc(promptKey)
                .orElseThrow(() -> new AiException("No active prompt found for key: " + promptKey));
        return renderTemplate(prompt.getPromptTemplate(), variables);
    }

    public AiPrompt getActivePrompt(String promptKey) {
        return promptRepository.findFirstByPromptKeyAndIsActiveTrueOrderByVersionDesc(promptKey)
                .orElseThrow(() -> new AiException("No active prompt found for key: " + promptKey));
    }

    private String renderTemplate(String template, Map<String, Object> variables) {
        try {
            Mustache mustache = mustacheFactory.compile(new StringReader(template), "prompt");
            StringWriter writer = new StringWriter();
            mustache.execute(writer, variables).flush();
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to render prompt template", e);
            throw new AiException("Prompt rendering failed", e);
        }
    }
}