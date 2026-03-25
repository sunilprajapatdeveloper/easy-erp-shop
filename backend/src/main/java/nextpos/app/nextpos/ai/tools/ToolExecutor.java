package nextpos.app.nextpos.ai.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.dto.AiResponse;
import nextpos.app.nextpos.ai.exception.AiException;
import nextpos.app.nextpos.model.entity.AiToolExecution;
import nextpos.app.nextpos.repository.AiToolExecutionRepository;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ToolExecutor {
    private final ToolRegistry toolRegistry;
    private final AiToolExecutionRepository toolExecutionRepository;

    public Object execute(UUID requestId, AiResponse.ToolCall toolCall) {
        Tool tool = toolRegistry.getTool(toolCall.getName());
        try {
            Object result = tool.execute(toolCall.getArguments());
            // Log execution
            AiToolExecution execution = new AiToolExecution();
            execution.setRequestId(requestId);
            execution.setToolName(toolCall.getName());
            execution.setToolInput(toolCall.getArguments());
            execution.setToolOutput(result);
            toolExecutionRepository.save(execution);
            return result;
        } catch (Exception e) {
            log.error("Tool execution failed: {}", toolCall.getName(), e);
            throw new AiException("Tool execution failed: " + toolCall.getName(), e);
        }
    }
}