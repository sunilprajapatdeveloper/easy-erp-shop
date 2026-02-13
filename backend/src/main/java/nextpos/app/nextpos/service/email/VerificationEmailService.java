package nextpos.app.nextpos.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.EmailRequest;
import nextpos.app.nextpos.model.enums.VerificationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationEmailService {

    private final MailService mailService;

    @Value("${app.verification.email.default-from:easyerpshop@gmail.com}")
    private String defaultFrom;

    @Value("${app.verification.email.support:easyerpshop@gmail.com}")
    private String supportEmail;

    @Value("${app.verification.email.app-name:EasyErpShop}")
    private String appName;

    @Value("#{${app.verification.email.subjects:{}}}")
    private Map<VerificationType, String> subjects;

    private static final Map<VerificationType, String> TEMPLATE_PATHS = Map.of(
            VerificationType.USER_REGISTRATION, "templates/email/verification.html",
            VerificationType.PASSWORD_RESET, "templates/email/verification.html",
            VerificationType.EMAIL_OTP_LOGIN, "templates/email/verification.html",
            VerificationType.EMAIL_CHANGE_CONFIRMATION, "templates/email/verification.html",
            VerificationType.COMPANY_VERIFICATION, "templates/email/company-verification.html",
            VerificationType.WAREHOUSE_VERIFICATION, "templates/email/warehouse-verification.html",
            VerificationType.TWO_FACTOR_AUTH, "templates/email/2fa.html");

    private final Map<String, String> pathCache = new ConcurrentHashMap<>();

    public void sendVerificationEmail(UUID verificationId, String email, String token,
            VerificationType type, Duration expiryDuration) {
        try {
            if (!isVerificationType(type)) {
                log.warn("Attempted to send non‑verification email via VerificationEmailService: {}", type);
                return;
            }

            String subject = getEmailSubject(type);
            String from = getFromAddress(type);
            String displayedToken = formatToken(token);
            String htmlContent = populateTemplate(type, getActionDescription(type), displayedToken,
                    expiryDuration.toMinutes());

            EmailRequest request = EmailRequest.builder()
                    .companyId(null)
                    .to(List.of(email))
                    .subject(subject)
                    .content(htmlContent)
                    .isHtml(true)
                    .from(from)
                    .replyTo(supportEmail)
                    .build();

            mailService.sendEmail(request);
            log.info("Verification email sent to {} (type={}, from={})", email, type, from);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String getTemplatePath(VerificationType type) {
        String path = TEMPLATE_PATHS.get(type);
        if (path == null) {
            throw new IllegalArgumentException(
                    "No email template configured for verification type: " + type + ". " +
                            "Please add an entry in the TEMPLATE_PATHS map inside VerificationEmailService.");
        }
        return path;
    }

    private String getTemplateContent(String path) {
        return pathCache.computeIfAbsent(path, p -> {
            try {
                ClassPathResource resource = new ClassPathResource(p);
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String content = new String(bytes, StandardCharsets.UTF_8);
                log.debug("Loaded email template: {}", p);
                return content;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load email template: " + p, e);
            }
        });
    }

    private String populateTemplate(VerificationType type, String action, String token, long expiryMinutes) {
        String path = getTemplatePath(type);
        String template = getTemplateContent(path);
        return template
                .replace("${action}", action)
                .replace("${token}", token)
                .replace("${expiryMinutes}", String.valueOf(expiryMinutes))
                .replace("${supportEmail}", supportEmail)
                .replace("${appName}", appName)
                .replace("${currentYear}", String.valueOf(LocalDateTime.now().getYear()));
    }

    private String getFromAddress(VerificationType type) {
        return switch (type) {
            case COMPANY_VERIFICATION, WAREHOUSE_VERIFICATION -> "easyerpshop@gmail.com";
            case TWO_FACTOR_AUTH -> "easyerpshop@gmail.com";
            default -> defaultFrom;
        };
    }

    private String getEmailSubject(VerificationType type) {
        if (subjects != null && subjects.containsKey(type)) {
            return subjects.get(type);
        }

        return switch (type) {
            case USER_REGISTRATION -> "Verify Your Account - " + appName;
            case PASSWORD_RESET -> "Password Reset Request - " + appName;
            case EMAIL_OTP_LOGIN -> "Your Login Code - " + appName;
            case EMAIL_CHANGE_CONFIRMATION -> "Confirm Email Change - " + appName;
            case COMPANY_VERIFICATION -> "Company Verification Required - " + appName;
            case WAREHOUSE_VERIFICATION -> "Warehouse Access Verification - " + appName;
            case TWO_FACTOR_AUTH -> "Two-Factor Authentication Code - " + appName;
            default -> "Verification Required - " + appName;
        };
    }

    private String formatToken(String token) {
        if (token == null)
            return "";
        if (token.matches("\\d+")) {
            return token.replaceAll("(.{4})", "$1 ").trim();
        }
        return token;
    }

    private String getActionDescription(VerificationType type) {
        return switch (type) {
            case USER_REGISTRATION -> "Complete Your Account Registration";
            case PASSWORD_RESET -> "Reset Your Password";
            case EMAIL_OTP_LOGIN -> "Login to Your Account";
            case EMAIL_CHANGE_CONFIRMATION -> "Confirm Your New Email Address";
            case COMPANY_VERIFICATION -> "Verify Your Company Account";
            case WAREHOUSE_VERIFICATION -> "Verify Warehouse Access";
            case TWO_FACTOR_AUTH -> "Two-Factor Authentication";
            default -> "Complete Verification";
        };
    }

    private boolean isVerificationType(VerificationType type) {
        return switch (type) {
            case USER_REGISTRATION, PASSWORD_RESET, EMAIL_OTP_LOGIN,
                    EMAIL_CHANGE_CONFIRMATION, COMPANY_VERIFICATION,
                    WAREHOUSE_VERIFICATION, TWO_FACTOR_AUTH ->
                true;
            default -> false;
        };
    }
}