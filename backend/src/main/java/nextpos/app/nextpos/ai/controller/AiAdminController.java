package nextpos.app.nextpos.ai.controller;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.ai.prompt.PromptVersionManager;
import nextpos.app.nextpos.model.entity.AiPrompt;
import nextpos.app.nextpos.model.entity.TenantAiSettings;
import nextpos.app.nextpos.repository.TenantAiSettingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/admin")
@RequiredArgsConstructor
public class AiAdminController {
    private final PromptVersionManager promptVersionManager;
    private final TenantAiSettingsRepository settingsRepository;

    @PostMapping("/prompts")
    public ResponseEntity<AiPrompt> createPrompt(@RequestBody Map<String, Object> payload) {
        // payload contains promptKey, template, description, createdBy
        AiPrompt prompt = promptVersionManager.createNewVersion(
                (String) payload.get("promptKey"),
                (String) payload.get("template"),
                (String) payload.get("description"),
                (String) payload.get("createdBy"));
        return ResponseEntity.ok(prompt);
    }

    @GetMapping("/prompts/{promptKey}/versions")
    public ResponseEntity<List<AiPrompt>> listVersions(@PathVariable String promptKey) {
        return ResponseEntity.ok(promptVersionManager.listVersions(promptKey));
    }

    @PutMapping("/tenants/{tenantId}/settings")
    public ResponseEntity<TenantAiSettings> updateTenantSettings(@PathVariable String tenantId,
            @RequestBody TenantAiSettings settings) {
        settings.setTenantId(tenantId);
        return ResponseEntity.ok(settingsRepository.save(settings));
    }

    @GetMapping("/tenants/{tenantId}/settings")
    public ResponseEntity<TenantAiSettings> getTenantSettings(@PathVariable String tenantId) {
        return ResponseEntity.ok(settingsRepository.findByTenantId(tenantId).orElseThrow());
    }
}