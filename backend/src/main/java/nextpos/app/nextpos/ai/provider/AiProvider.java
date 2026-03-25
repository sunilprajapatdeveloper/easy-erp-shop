package nextpos.app.nextpos.ai.provider;

import nextpos.app.nextpos.ai.dto.AiResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface AiProvider {
    AiResponse<String> complete(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options);

    Flux<String> stream(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options);

    String getProviderName();

    record Message(String role, String content) {
    }

    record ToolDefinition(String name, String description, Map<String, Object> parameters) {
    }

    record AiRequestOptions(String model, double temperature, int maxTokens, int timeoutMs) {
    }
}