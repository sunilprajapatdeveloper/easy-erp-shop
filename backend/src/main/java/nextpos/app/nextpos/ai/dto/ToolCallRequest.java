package nextpos.app.nextpos.ai.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ToolCallRequest {
    private UUID requestId;
    private String toolName;
    private Map<String, Object> arguments;
}