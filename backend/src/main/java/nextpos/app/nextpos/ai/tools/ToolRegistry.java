package nextpos.app.nextpos.ai.tools;

import org.springframework.stereotype.Component;

import nextpos.app.nextpos.ai.provider.AiProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ToolRegistry {
    private final Map<String, Tool> tools = new HashMap<>();

    public void register(Tool tool) {
        tools.put(tool.getName(), tool);
    }

    public Tool getTool(String name) {
        Tool tool = tools.get(name);
        if (tool == null) {
            throw new IllegalArgumentException("Tool not found: " + name);
        }
        return tool;
    }

    public List<ToolDefinition> getToolDefinitions() {
        return tools.values().stream()
                .map(tool -> new ToolDefinition(tool.getName(), tool.getDescription(), tool.getParameters()))
                .collect(Collectors.toList());
    }

    public List<AiProvider.ToolDefinition> getProviderToolDefinitions() {
        return tools.values().stream()
                .map(tool -> new AiProvider.ToolDefinition(tool.getName(), tool.getDescription(), tool.getParameters()))
                .collect(Collectors.toList());
    }
}