package nextpos.app.nextpos.ai.cost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.exception.QuotaExceededException;
import nextpos.app.nextpos.model.entity.TenantAiSettings;
import nextpos.app.nextpos.repository.TenantAiSettingsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuotaEnforcer {
    private final TenantAiSettingsRepository settingsRepository;

    public void checkQuota(String tenantId, long estimatedTokens) {
        Optional<TenantAiSettings> settingsOpt = settingsRepository.findByTenantId(tenantId);
        if (settingsOpt.isEmpty()) {
            throw new QuotaExceededException("Tenant AI settings not found");
        }
        TenantAiSettings settings = settingsOpt.get();
        long used = settings.getTokensUsedCurrentMonth();
        long max = settings.getMaxTokensPerMonth();
        if (used + estimatedTokens > max) {
            log.warn("Tenant {} quota exceeded: used={}, max={}, requested={}", tenantId, used, max, estimatedTokens);
            throw new QuotaExceededException("Monthly token quota exceeded");
        }
    }
}