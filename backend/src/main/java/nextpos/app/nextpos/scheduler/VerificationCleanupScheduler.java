package nextpos.app.nextpos.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.VerificationProperties;
import nextpos.app.nextpos.model.entity.EmailVerification;
import nextpos.app.nextpos.model.entity.VerificationAttempt;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.repository.EmailVerificationRepository;
import nextpos.app.nextpos.repository.VerificationAttemptRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationCleanupScheduler {

    private final EmailVerificationRepository verificationRepository;
    private final VerificationAttemptRepository attemptRepository;
    private final VerificationProperties verificationProperties;

    @Scheduled(cron = "${verification.cleanup.cron:0 0 2 * * ?}")
    @Transactional
    public void cleanupExpiredVerifications() {
        LocalDateTime now = LocalDateTime.now();

        // Expire stale verifications
        int expiredCount = verificationRepository.expireStaleVerifications(now);

        // Archive and remove old verification attempts
        LocalDateTime attemptThreshold = now.minusDays(30);
        List<VerificationAttempt> oldAttempts = attemptRepository.findByAttemptedAtBefore(attemptThreshold);
        attemptRepository.deleteAll(oldAttempts);

        // Archive old verifications
        LocalDateTime retentionThreshold = now.minus(verificationProperties.getCleanup().getRetentionPeriod());
        List<EmailVerification> oldVerifications = verificationRepository
                .findByStatusAndCreatedAtBefore(VerificationStatus.VERIFIED, retentionThreshold);

        if (expiredCount > 0 || !oldAttempts.isEmpty() || !oldVerifications.isEmpty()) {
            log.info(
                    "Cleanup completed: Expired {} verifications, removed {} old attempts, archived {} old verifications",
                    expiredCount, oldAttempts.size(), oldVerifications.size());
        }
    }

    @Scheduled(cron = "0 */5 * * * ?") // Every 5 minutes
    @Transactional
    public void cleanupExpiredInRealTime() {
        LocalDateTime now = LocalDateTime.now();
        int expiredCount = verificationRepository.expireStaleVerifications(now);

        if (expiredCount > 0) {
            log.debug("Real-time cleanup: Expired {} verification(s)", expiredCount);
        }
    }
}