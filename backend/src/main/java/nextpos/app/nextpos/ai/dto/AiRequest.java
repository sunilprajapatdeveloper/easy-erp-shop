package nextpos.app.nextpos.ai.dto;

import lombok.Data;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;

@Data
public class AiRequest {
    @NotBlank
    private String query; // user input or prompt variables
    private Map<String, Object> context; // optional context, e.g., productId, warehouseId
    private Boolean stream = false; // whether to stream response
    private String promptKey; // optional, override default prompt key for feature
    private Map<String, Object> variables; // additional variables for prompt template
}