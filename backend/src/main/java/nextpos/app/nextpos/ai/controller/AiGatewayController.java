package nextpos.app.nextpos.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.dto.AiRequest;
import nextpos.app.nextpos.ai.dto.AiResponse;
import nextpos.app.nextpos.ai.orchestrator.AiOrchestrator;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import reactor.core.publisher.Flux;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiGatewayController {
    private final AiOrchestrator orchestrator;
    private final UserRepository userRepository;

    @PostMapping("/chat")
    public ResponseEntity<AiResponse<?>> chat(@Valid @RequestBody AiRequest request) {
        // Assuming UserContext has methods getCompanyId() and getUserId()
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        String tenantId = String.valueOf(companyId);
        String userId = String.valueOf(user.getId());

        AiResponse<?> response = orchestrator.orchestrate(tenantId, userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat/stream")
    public Flux<String> chatStream(@Valid @RequestBody AiRequest request) {
        // Implement streaming using Flux
        throw new UnsupportedOperationException("Streaming not implemented yet");
    }

    @PostMapping("/product/generate")
    public ResponseEntity<AiResponse<?>> generateProduct(@Valid @RequestBody AiRequest request) {
        // Similar to chat, but with different prompt key
        request.setPromptKey("product_generation");
        return chat(request);
    }
}