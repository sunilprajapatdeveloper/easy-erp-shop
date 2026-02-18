package nextpos.app.nextpos.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.VerificationProperties;
import nextpos.app.nextpos.exception.*;
import nextpos.app.nextpos.model.dto.request.VerificationRequest;
import nextpos.app.nextpos.model.dto.request.VerificationValidationRequest;
import nextpos.app.nextpos.model.dto.response.VerificationCreationResponse;
import nextpos.app.nextpos.model.dto.response.VerificationResult;
import nextpos.app.nextpos.model.entity.EmailVerification;
import nextpos.app.nextpos.model.entity.VerificationAttempt;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.model.enums.VerificationType;
import nextpos.app.nextpos.repository.EmailVerificationRepository;
import nextpos.app.nextpos.repository.VerificationAttemptRepository;
import nextpos.app.nextpos.service.interf.EmailVerificationService;
import nextpos.app.nextpos.service.queue.VerificationQueuePublisher;
import nextpos.app.nextpos.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository verificationRepository;
    private final VerificationAttemptRepository attemptRepository;
    private final VerificationQueuePublisher queuePublisher;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationProperties verificationProperties;
    private final HttpServletRequest httpServletRequest;

    private final Map<VerificationType, Duration> expiryConfig = Map.of(
            VerificationType.USER_REGISTRATION, Duration.ofHours(24),
            VerificationType.PASSWORD_RESET, Duration.ofMinutes(15),
            VerificationType.EMAIL_OTP_LOGIN, Duration.ofMinutes(10),
            VerificationType.COMPANY_VERIFICATION, Duration.ofHours(72),
            VerificationType.EMAIL_CHANGE_CONFIRMATION, Duration.ofHours(2),
            VerificationType.TWO_FACTOR_AUTH, Duration.ofMinutes(5),
            VerificationType.TRANSACTION_CONFIRMATION, Duration.ofMinutes(30),
            VerificationType.DOCUMENT_APPROVAL, Duration.ofHours(48),
            VerificationType.WAREHOUSE_VERIFICATION, Duration.ofHours(24));

    @Override
    @Transactional
    public VerificationCreationResponse createVerification(VerificationRequest request) {
        log.info("Creating verification request for email: {}, type: {}",
                request.getEmail(), request.getVerificationType());

        // Check rate limits
        checkRateLimits(request.getEmail(), request.getVerificationType());

        // Invalidate existing pending verifications
        invalidateExistingVerifications(request.getEmail(), request.getVerificationType());

        // Generate secure token
        String rawToken = tokenGenerator.generateNumericOTP(6);
        String tokenHash = passwordEncoder.encode(rawToken);

        // Determine expiry duration
        Duration expiryDuration = expiryConfig.getOrDefault(
                request.getVerificationType(),
                verificationProperties.getToken().getDefaultExpiry());

        // Create verification entity
        EmailVerification verification = EmailVerification.builder()
                .email(request.getEmail())
                .tokenHash(tokenHash)
                .verificationType(request.getVerificationType())
                .status(VerificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(expiryDuration))
                .maxAttempts(verificationProperties.getToken().getDefaultMaxAttempts())
                .referenceId(request.getReferenceId())
                .referenceType(request.getReferenceType())
                .metadata(request.getMetadata())
                .build();

        verification = verificationRepository.save(verification);

        // Publish to Kafka queue for async email sending
        try {
            queuePublisher.publishVerificationEmail(verification.getId(), request.getEmail(), rawToken,
                    request.getVerificationType(), expiryDuration);
            log.info("Verification email queued for email: {}, verificationId: {}", request.getEmail(),
                    verification.getId());
        } catch (Exception e) {
            log.error("Failed to queue verification email for: {}", request.getEmail(), e);
            throw new VerificationException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR,
                    "EMAIL_QUEUE_FAILED");
        }

        return VerificationCreationResponse.builder()
                .verificationId(verification.getId())
                .expiresAt(verification.getExpiresAt())
                .message("Verification email sent successfully")
                .build();
    }

    @Override
    @Transactional
    public VerificationResult validateVerification(VerificationValidationRequest request) {
        log.info("Validating verification for email: {}, type: {}",
                request.getEmail(), request.getVerificationType());

        // Find pending verification
        List<EmailVerification> verifications = verificationRepository
                .findByEmailAndVerificationTypeAndStatus(
                        request.getEmail(),
                        request.getVerificationType(),
                        VerificationStatus.PENDING);

        if (verifications.isEmpty()) {
            logAttempt(request.getEmail(), null, "NO_PENDING_VERIFICATION",
                    request.getVerificationType(), request.getToken());
            throw new VerificationNotFoundException("No pending verification found");
        }

        EmailVerification verification = verifications.get(0);

        // Check if already processed
        if (verification.getStatus() == VerificationStatus.VERIFIED) {
            return buildSuccessResult(verification, "Already verified");
        }

        // Check expiry
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            verification.setStatus(VerificationStatus.EXPIRED);
            verificationRepository.save(verification);
            logAttempt(request.getEmail(), verification.getId(), "TOKEN_EXPIRED",
                    request.getVerificationType(), request.getToken());
            throw new VerificationExpiredException("Verification token has expired");
        }

        // Check attempt limits
        if (verification.getAttemptCount() >= verification.getMaxAttempts()) {
            verification.setStatus(VerificationStatus.LOCKED);
            verificationRepository.save(verification);
            logAttempt(request.getEmail(), verification.getId(), "TOO_MANY_ATTEMPTS",
                    request.getVerificationType(), request.getToken());
            throw new VerificationAttemptsExceededException("Too many verification attempts");
        }

        // Increment attempt count
        verification.setAttemptCount(verification.getAttemptCount() + 1);
        verification.setLastAttemptAt(LocalDateTime.now());
        verificationRepository.save(verification);

        // Validate token
        if (!passwordEncoder.matches(request.getToken(), verification.getTokenHash())) {
            logAttempt(request.getEmail(), verification.getId(), "INVALID_TOKEN",
                    request.getVerificationType(), request.getToken());

            if (verification.getAttemptCount() >= verification.getMaxAttempts()) {
                verification.setStatus(VerificationStatus.LOCKED);
                verificationRepository.save(verification);
                throw new VerificationAttemptsExceededException("Too many attempts - verification locked");
            }

            throw new VerificationException("Invalid verification token");
        }

        // Mark as verified
        verification.setStatus(VerificationStatus.VERIFIED);
        verification.setVerifiedAt(LocalDateTime.now());
        verificationRepository.save(verification);

        log.info("Successfully verified email: {}, type: {}, verificationId: {}",
                verification.getEmail(), verification.getVerificationType(), verification.getId());

        return buildSuccessResult(verification, "Successfully verified");
    }

    @Override
    @Transactional
    public void resendVerification(String email, VerificationType verificationType) {
        log.info("Resending verification for email: {}, type: {}", email, verificationType);

        // Check rate limits for resend
        if (isRateLimited(email)) {
            throw new VerificationException("Too many resend requests. Please try again later.");
        }

        // Find and revoke existing pending verifications
        List<EmailVerification> existing = verificationRepository
                .findByEmailAndVerificationTypeAndStatus(email, verificationType, VerificationStatus.PENDING);

        existing.forEach(v -> {
            v.setStatus(VerificationStatus.REVOKED);
            verificationRepository.save(v);
        });

        // Create new verification
        VerificationRequest request = new VerificationRequest();
        request.setEmail(email);
        request.setVerificationType(verificationType);

        createVerification(request);
    }

    @Override
    @Transactional
    public void revokeVerification(String verificationId) {
        log.info("Revoking verification: {}", verificationId);

        try {
            UUID uuid = UUID.fromString(verificationId);
            verificationRepository.findById(uuid)
                    .ifPresent(verification -> {
                        verification.setStatus(VerificationStatus.REVOKED);
                        verificationRepository.save(verification);
                        log.info("Successfully revoked verification: {}", verificationId);
                    });
        } catch (IllegalArgumentException e) {
            throw new VerificationException("Invalid verification ID format");
        }
    }

    @Override
    public VerificationStatus checkStatus(String verificationId) {
        try {
            UUID uuid = UUID.fromString(verificationId);
            return verificationRepository.findById(uuid)
                    .map(EmailVerification::getStatus)
                    .orElseThrow(() -> new VerificationNotFoundException("Verification not found"));
        } catch (IllegalArgumentException e) {
            throw new VerificationException("Invalid verification ID format");
        }
    }

    private void checkRateLimits(String email, VerificationType type) {
        LocalDateTime lastHour = LocalDateTime.now().minusHours(1);
        Long recentAttempts = verificationRepository.countByEmailAndCreatedAtAfter(email, lastHour);

        int maxRequests = verificationProperties.getSecurity().getMaxRequestsPerHour();
        if (recentAttempts >= maxRequests) {
            throw new VerificationException(
                    String.format("Too many verification requests. Maximum %d per hour allowed.", maxRequests),
                    HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED");
        }
    }

    private boolean isRateLimited(String email) {
        LocalDateTime cooldownStart = LocalDateTime.now()
                .minus(verificationProperties.getSecurity().getCooldownPeriod());

        List<EmailVerification> recentVerifications = verificationRepository
                .findByEmailAndStatus(email, VerificationStatus.PENDING);

        return recentVerifications.stream()
                .anyMatch(v -> v.getCreatedAt().isAfter(cooldownStart));
    }

    private void invalidateExistingVerifications(String email, VerificationType type) {
        List<EmailVerification> existing = verificationRepository
                .findByEmailAndVerificationTypeAndStatus(email, type, VerificationStatus.PENDING);

        existing.forEach(v -> {
            v.setStatus(VerificationStatus.REVOKED);
            verificationRepository.save(v);
            log.debug("Invalidated existing verification: {} for email: {}", v.getId(), email);
        });
    }

    private void logAttempt(String email, UUID verificationId, String status,
            VerificationType type, String attemptedToken) {
        String ipAddress = getClientIpAddress();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        VerificationAttempt attempt = VerificationAttempt.builder()
                .verificationId(verificationId)
                .attemptedTokenHash(attemptedToken != null ? passwordEncoder.encode(attemptedToken) : null)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .attemptStatus(status)
                .attemptedAt(LocalDateTime.now())
                .failureReason(status)
                .build();

        attemptRepository.save(attempt);
        log.debug("Logged verification attempt: {} for email: {}", status, email);
    }

    private String getClientIpAddress() {
        String xForwardedForHeader = httpServletRequest.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        return httpServletRequest.getRemoteAddr();
    }

    private VerificationResult buildSuccessResult(EmailVerification verification, String message) {
        VerificationResult result = new VerificationResult();
        result.setSuccess(true);
        result.setEmail(verification.getEmail());
        result.setStatus(verification.getStatus());
        result.setVerifiedAt(verification.getVerifiedAt());
        result.setReferenceId(verification.getReferenceId());
        result.setReferenceType(verification.getReferenceType());
        result.setMessage(message);
        return result;
    }
}