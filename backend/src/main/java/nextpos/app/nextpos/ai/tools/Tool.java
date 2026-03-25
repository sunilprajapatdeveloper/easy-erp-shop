package nextpos.app.nextpos.ai.tools;

import java.util.Map;

public interface Tool {
    String getName();

    String getDescription();

    Map<String, Object> getParameters(); // JSON Schema

    Object execute(Map<String, Object> arguments);
}