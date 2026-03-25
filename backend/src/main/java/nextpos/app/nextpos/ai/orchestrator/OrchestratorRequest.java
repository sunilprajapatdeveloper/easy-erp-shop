package nextpos.app.nextpos.ai.orchestrator;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class OrchestratorRequest {
    private UUID requestId;
    private String tenantId;
    private String userId;
    private String promptKey;
    private Map<String, Object> variables;
    private Map<String, Object> context;
    private boolean stream;
}