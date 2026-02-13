package nextpos.app.nextpos.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.EmailRequest;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.SMTPSettings;
import nextpos.app.nextpos.repository.CompanyRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final SMTPService smtpService;
    private final CompanyRepository companyRepository;

    private static final String PASSWORD_TEMPLATE_PATH = "templates/email/password-created.html";
    private static final String NOTIFICATION_TEMPLATE_PATH = "templates/email/notification.html";

    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    private String loadTemplate(String path) {
        return templateCache.computeIfAbsent(path, p -> {
            try {
                ClassPathResource resource = new ClassPathResource(p);
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load email template: " + p, e);
            }
        });
    }

    /**
     * Main method to send emails with company-specific or default SMTP
     * configuration
     */
    @Async
    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public CompletableFuture<Void> sendEmail(EmailRequest request) {
        return CompletableFuture.runAsync(() -> {
            try {
                validateEmailRequest(request);
                sendEmailInternal(request);
                log.debug("Email sent successfully for companyId: {}", request.getCompanyId());
            } catch (Exception e) {
                log.error("Failed to send email for companyId: {}", request.getCompanyId(), e);
                throw new EmailException("Failed to send email", e);
            }
        });
    }

    /**
     * Legacy method for backward compatibility
     */
    @Async
    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendHtmlEmail(Long companyId, String subject, String htmlContent, String... to)
            throws MessagingException {
        EmailRequest request = EmailRequest.builder()
                .companyId(companyId)
                .to(Arrays.asList(to))
                .subject(subject)
                .content(htmlContent)
                .isHtml(true)
                .build();

        sendEmail(request);
    }

    /**
     * Legacy method for backward compatibility
     */
    @Async
    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendPlainTextEmail(Long companyId, String subject, String textContent, String... to)
            throws MessagingException {
        EmailRequest request = EmailRequest.builder()
                .companyId(companyId)
                .to(Arrays.asList(to))
                .subject(subject)
                .content(textContent)
                .isHtml(false)
                .build();

        sendEmail(request);
    }

    /**
     * Send email to multiple recipients with company context
     */
    @Async
    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public CompletableFuture<Void> sendCompanyEmail(Long companyId, List<String> to,
            String subject, String content) {
        EmailRequest request = EmailRequest.builder()
                .companyId(companyId)
                .to(to)
                .subject(subject)
                .content(content)
                .isHtml(true)
                .build();

        return sendEmail(request);
    }

    /**
     * System-level emails (using default SMTP configuration)
     */
    @Async
    public CompletableFuture<Void> sendSystemEmail(List<String> to, String subject, String content) {
        EmailRequest request = EmailRequest.builder()
                .companyId(null) // Will use default configuration
                .to(to)
                .subject(subject)
                .content(content)
                .isHtml(true)
                .build();

        return sendEmail(request);
    }

    /**
     * Internal email sending implementation
     */
    private void sendEmailInternal(EmailRequest request) throws MessagingException {
        // Get appropriate mail sender
        JavaMailSender mailSender = smtpService.getMailSender(request.getCompanyId());
        SMTPSettings smtpSettings = smtpService.getSMTPSettingsForCompany(request.getCompanyId());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, request.isHtml(), "UTF-8");

        // Set from address with company name if available
        setFromAddress(helper, smtpSettings, request);

        // Set recipients
        helper.setTo(request.getTo().toArray(new String[0]));

        if (!CollectionUtils.isEmpty(request.getCc())) {
            helper.setCc(request.getCc().toArray(new String[0]));
        }

        if (!CollectionUtils.isEmpty(request.getBcc())) {
            helper.setBcc(request.getBcc().toArray(new String[0]));
        }

        // Set reply-to if provided
        if (StringUtils.hasText(request.getReplyTo())) {
            helper.setReplyTo(request.getReplyTo());
        }

        helper.setSubject(request.getSubject());
        helper.setText(request.getContent(), request.isHtml());

        mailSender.send(message);
    }

    /**
     * Set from address with company context
     */
    private void setFromAddress(MimeMessageHelper helper, SMTPSettings settings, EmailRequest request)
            throws MessagingException {
        try {
            if (StringUtils.hasText(request.getFrom())) {
                helper.setFrom(request.getFrom());
                return;
            }

            Long companyId = request.getCompanyId();
            if (companyId != null) {
                Company company = companyRepository.findById(companyId).orElse(null);
                if (company != null && StringUtils.hasText(company.getCompanyName())) {
                    helper.setFrom(settings.getFromEmail(), company.getCompanyName());
                    return;
                }
            }

            if (StringUtils.hasText(settings.getFromName())) {
                helper.setFrom(settings.getFromEmail(), settings.getFromName());
            } else {
                helper.setFrom(settings.getFromEmail());
            }
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Failed to set from address with personal name", e);
        }
    }

    /**
     * Validate email request
     */
    private void validateEmailRequest(EmailRequest request) {
        if (CollectionUtils.isEmpty(request.getTo())) {
            throw new IllegalArgumentException("Email must have at least one recipient");
        }

        // Validate each email address
        request.getTo().forEach(this::validateEmailFormat);

        if (!CollectionUtils.isEmpty(request.getCc())) {
            request.getCc().forEach(this::validateEmailFormat);
        }

        if (!CollectionUtils.isEmpty(request.getBcc())) {
            request.getBcc().forEach(this::validateEmailFormat);
        }
    }

    /**
     * Basic email format validation
     */
    private void validateEmailFormat(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email address format: " + email);
        }
    }

    /**
     * Test SMTP connection for a company
     */
    public boolean testSMTPConnection(Long companyId) {
        try {
            JavaMailSender mailSender = smtpService.getMailSender(companyId);
            if (mailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
                ((org.springframework.mail.javamail.JavaMailSenderImpl) mailSender).testConnection();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("SMTP connection test failed for companyId: {}", companyId, e);
            return false;
        }
    }

    /**
     * Clear cached mail sender for a company (call when SMTP settings change)
     */
    public void refreshCompanySMTP(Long companyId) {
        smtpService.clearCache(companyId);
        log.info("Refreshed SMTP configuration for companyId: {}", companyId);
    }

    /**
     * Build password reset email with company branding – NOW USING EXTERNAL
     * TEMPLATE
     */
    public String buildPasswordEmail(String password, String companyName) {
        String template = loadTemplate(PASSWORD_TEMPLATE_PATH);
        return template
                .replace("${companyName}", companyName)
                .replace("${password}", password)
                .replace("${currentYear}", String.valueOf(Year.now().getValue()));
    }

    /**
     * Build notification email template – NOW USING EXTERNAL TEMPLATE
     */
    public String buildNotificationEmail(String title, String message, String companyName) {
        String template = loadTemplate(NOTIFICATION_TEMPLATE_PATH);
        return template
                .replace("${title}", title)
                .replace("${message}", message)
                .replace("${companyName}", companyName)
                .replace("${currentYear}", String.valueOf(Year.now().getValue()));
    }

    // Custom exception
    public static class EmailException extends RuntimeException {
        public EmailException(String message) {
            super(message);
        }

        public EmailException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}