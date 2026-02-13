package nextpos.app.nextpos.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.SMTPSettings;
import nextpos.app.nextpos.repository.SMTPSettingsRepository;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMTPService {

    private final SMTPSettingsRepository smtpSettingsRepository;
    private final MailProperties defaultMailProperties;

    private static final Long SYSTEM_DEFAULT_KEY = -1L;

    private final ConcurrentHashMap<Long, JavaMailSender> mailSenderCache = new ConcurrentHashMap<>();

    public SMTPSettings getSMTPSettingsForCompany(Long companyId) {
        if (companyId == null) {
            return getDefaultSystemSettings();
        }

        return smtpSettingsRepository.findByCompanyId(companyId)
                .orElseGet(this::getDefaultSystemSettings);
    }

    private SMTPSettings getDefaultSystemSettings() {
        String defaultFrom = defaultMailProperties.getProperties().get("mail.from");
        if (!StringUtils.hasText(defaultFrom)) {
            defaultFrom = defaultMailProperties.getUsername();
        }

        return SMTPSettings.builder()
                .host(defaultMailProperties.getHost())
                .port(defaultMailProperties.getPort())
                .username(defaultMailProperties.getUsername())
                .password(defaultMailProperties.getPassword())
                .fromEmail(defaultFrom)
                .sslEnabled(Boolean.parseBoolean(
                        defaultMailProperties.getProperties().getOrDefault("mail.smtp.ssl.enable", "false")))
                .tlsEnabled(Boolean.parseBoolean(
                        defaultMailProperties.getProperties().getOrDefault("mail.smtp.starttls.enable", "false")))
                .connectionTimeout(5000)
                .timeout(5000)
                .writeTimeout(5000)
                .build();
    }

    public JavaMailSender getMailSender(Long companyId) {
        Long cacheKey = (companyId == null) ? SYSTEM_DEFAULT_KEY : companyId;

        return mailSenderCache.computeIfAbsent(cacheKey, key -> {
            SMTPSettings settings;
            if (key.equals(SYSTEM_DEFAULT_KEY)) {
                settings = getDefaultSystemSettings();
            } else {
                settings = getSMTPSettingsForCompany(key);
            }
            return createMailSender(settings);
        });
    }

    private JavaMailSender createMailSender(SMTPSettings settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(settings.isTlsEnabled()));
        props.put("mail.smtp.ssl.enable", String.valueOf(settings.isSslEnabled()));
        props.put("mail.smtp.connectiontimeout", settings.getConnectionTimeout());
        props.put("mail.smtp.timeout", settings.getTimeout());
        props.put("mail.smtp.writetimeout", settings.getWriteTimeout());
        props.put("mail.debug", "false");

        return mailSender;
    }

    public void clearCache(Long companyId) {
        if (companyId == null) {
            mailSenderCache.remove(SYSTEM_DEFAULT_KEY);
        } else {
            mailSenderCache.remove(companyId);
        }
        log.debug("Cleared mail sender cache for companyId: {}", companyId);
    }
}