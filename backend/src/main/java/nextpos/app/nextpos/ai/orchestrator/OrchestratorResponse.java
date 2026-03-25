package nextpos.app.nextpos.ai.orchestrator;

import lombok.Builder;
import lombok.Data;
import nextpos.app.nextpos.ai.dto.AiResponse;

@Data
@Builder
public class OrchestratorResponse {
    private AiResponse<?> response;
    private boolean cached;
}