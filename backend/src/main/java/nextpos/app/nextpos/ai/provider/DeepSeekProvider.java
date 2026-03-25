package nextpos.app.nextpos.ai.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.config.AiConfig;
import nextpos.app.nextpos.ai.dto.AiResponse;
import nextpos.app.nextpos.ai.exception.ProviderException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DeepSeekProvider implements AiProvider {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final AiConfig.ProviderConfig config;

    @Override
    public AiResponse<String> complete(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", options.model());
            requestBody.put("temperature", options.temperature());
            requestBody.put("max_tokens", options.maxTokens());
            ArrayNode messagesArray = requestBody.putArray("messages");
            for (Message msg : messages) {
                ObjectNode msgNode = messagesArray.addObject();
                msgNode.put("role", msg.role());
                msgNode.put("content", msg.content());
            }
            if (tools != null && !tools.isEmpty()) {
                ArrayNode toolsArray = requestBody.putArray("tools");
                for (ToolDefinition tool : tools) {
                    ObjectNode toolNode = toolsArray.addObject();
                    toolNode.put("type", "function");
                    ObjectNode functionNode = toolNode.putObject("function");
                    functionNode.put("name", tool.name());
                    functionNode.put("description", tool.description());
                    functionNode.set("parameters", objectMapper.valueToTree(tool.parameters()));
                }
            }

            long startTime = System.currentTimeMillis();
            String response = webClient.post()
                    .uri("/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofMillis(options.timeoutMs()));
            long latency = System.currentTimeMillis() - startTime;

            JsonNode root = objectMapper.readTree(response);
            JsonNode choice = root.get("choices").get(0);
            JsonNode messageNode = choice.get("message");

            String content = messageNode.has("content") ? messageNode.get("content").asText() : null;
            int inputTokens = root.get("usage").get("prompt_tokens").asInt();
            int outputTokens = root.get("usage").get("completion_tokens").asInt();

            List<AiResponse.ToolCall> toolCalls = null;
            if (messageNode.has("tool_calls")) {
                toolCalls = new ArrayList<>();
                for (JsonNode tc : messageNode.get("tool_calls")) {
                    JsonNode func = tc.get("function");
                    toolCalls.add(AiResponse.ToolCall.builder()
                            .id(tc.get("id").asText())
                            .name(func.get("name").asText())
                            .arguments(objectMapper.readValue(func.get("arguments").asText(),
                                    new TypeReference<Map<String, Object>>() {
                                    }))
                            .build());
                }
            }

            return AiResponse.<String>builder()
                    .result(content)
                    .tokensUsed(inputTokens + outputTokens)
                    .model(options.model())
                    .latencyMs(latency)
                    .cached(false)
                    .toolCalls(toolCalls)
                    .build();

        } catch (Exception e) {
            log.error("DeepSeek call failed", e);
            throw new ProviderException("DeepSeek provider error", e);
        }
    }

    @Override
    public Flux<String> stream(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        throw new UnsupportedOperationException("Streaming not implemented yet");
    }

    @Override
    public String getProviderName() {
        return "deepseek";
    }
}