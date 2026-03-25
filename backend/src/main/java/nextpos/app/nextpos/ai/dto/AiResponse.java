package nextpos.app.nextpos.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse<T> {
    private T result;
    private Integer tokensUsed;
    private String model;
    private Long latencyMs;
    private Boolean cached;
    private List<ToolCall> toolCalls;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCall {
        private String id;
        private String name;
        private Map<String, Object> arguments;
    }
}