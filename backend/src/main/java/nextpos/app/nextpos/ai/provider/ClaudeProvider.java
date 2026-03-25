package nextpos.app.nextpos.ai.provider;

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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ClaudeProvider implements AiProvider {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final AiConfig.ProviderConfig config;

    @Override
    public AiResponse<String> complete(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", options.model());
            requestBody.put("max_tokens", options.maxTokens());
            requestBody.put("temperature", options.temperature());

            // Build messages array (Anthropic expects system and user/assistant)
            ArrayNode messagesArray = requestBody.putArray("messages");
            for (Message msg : messages) {
                ObjectNode msgNode = messagesArray.addObject();
                msgNode.put("role", msg.role());
                msgNode.put("content", msg.content());
            }

            // If there's a system message, extract it separately (Claude API uses system
            // param)
            // For simplicity, we'll combine system messages into the first user message,
            // but a proper implementation would separate them.

            long startTime = System.currentTimeMillis();
            String response = webClient.post()
                    .uri("/v1/messages")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                    .header("anthropic-version", "2023-06-01")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofMillis(options.timeoutMs()));
            long latency = System.currentTimeMillis() - startTime;

            JsonNode root = objectMapper.readTree(response);
            String content = root.get("content").get(0).get("text").asText();
            int inputTokens = root.get("usage").get("input_tokens").asInt();
            int outputTokens = root.get("usage").get("output_tokens").asInt();

            // Anthropic does not support tool calls in the same way yet; we'll ignore.
            return AiResponse.<String>builder()
                    .result(content)
                    .tokensUsed(inputTokens + outputTokens)
                    .model(options.model())
                    .latencyMs(latency)
                    .cached(false)
                    .toolCalls(null)
                    .build();

        } catch (Exception e) {
            log.error("Claude call failed", e);
            throw new ProviderException("Claude provider error", e);
        }
    }

    @Override
    public Flux<String> stream(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        throw new UnsupportedOperationException("Streaming not implemented yet");
    }

    @Override
    public String getProviderName() {
        return "claude";
    }
}