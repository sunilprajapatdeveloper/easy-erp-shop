package nextpos.app.nextpos.ai.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ToolDefinition {
    private String name;
    private String description;
    private Map<String, Object> parameters;
}