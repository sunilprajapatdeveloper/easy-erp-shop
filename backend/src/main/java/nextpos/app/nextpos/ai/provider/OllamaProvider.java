package nextpos.app.nextpos.ai.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.dto.AiResponse;
import nextpos.app.nextpos.ai.exception.ProviderException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Slf4j
public class OllamaProvider implements AiProvider {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OllamaProvider(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public AiResponse<String> complete(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        try {
            // Combine all messages into a single prompt (Ollama doesn't have multi‑role
            // chat)
            StringBuilder prompt = new StringBuilder();
            for (Message msg : messages) {
                prompt.append(msg.role()).append(": ").append(msg.content()).append("\n");
            }

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", options.model());
            requestBody.put("prompt", prompt.toString());
            requestBody.put("stream", false);
            if (options.temperature() > 0) {
                requestBody.put("temperature", options.temperature());
            }

            long startTime = System.currentTimeMillis();
            String response = webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofMillis(options.timeoutMs()));
            long latency = System.currentTimeMillis() - startTime;

            JsonNode root = objectMapper.readTree(response);
            String content = root.get("response").asText();

            // Rough token estimation
            int estimatedTokens = prompt.length() / 4 + content.length() / 4;

            return AiResponse.<String>builder()
                    .result(content)
                    .tokensUsed(estimatedTokens)
                    .model(options.model())
                    .latencyMs(latency)
                    .cached(false)
                    .toolCalls(null)
                    .build();

        } catch (Exception e) {
            log.error("Ollama call failed", e);
            throw new ProviderException("Ollama provider error", e);
        }
    }

    @Override
    public Flux<String> stream(List<Message> messages, List<ToolDefinition> tools, AiRequestOptions options) {
        throw new UnsupportedOperationException("Streaming not implemented yet");
    }

    @Override
    public String getProviderName() {
        return "ollama";
    }
}