package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.ResourceNotFoundException;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSMTPSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SMTPSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.SMTPSettings;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.SMTPSettingsRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.email.MailService;
import nextpos.app.nextpos.service.interf.SMTPSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SMTPSettingsServiceImpl implements SMTPSettingsService {

    private final SMTPSettingsRepository smtpSettingsRepository;
    private final CompanyRepository companyRepository;
    private final MailService mailService;

    @Override
    public SMTPSettingsResponse createOrUpdateSMTPSettings(CreateSMTPSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        SMTPSettings settings = smtpSettingsRepository.findByCompanyId(companyId)
                .orElse(SMTPSettings.builder().company(company).build());

        settings.setProvider(request.getProvider());
        settings.setHost(request.getHost());
        settings.setPort(request.getPort());
        settings.setUsername(request.getUsername());
        settings.setPassword(request.getPassword());
        settings.setSslEnabled(request.isSslEnabled());
        settings.setTlsEnabled(request.isTlsEnabled());
        settings.setFromEmail(request.getFromEmail());
        settings.setFromName(request.getFromName());
        settings.setConnectionTimeout(request.getConnectionTimeout());
        settings.setTimeout(request.getTimeout());
        settings.setWriteTimeout(request.getWriteTimeout());
        settings.setActive(true);

        SMTPSettings saved = smtpSettingsRepository.save(settings);
        mailService.refreshCompanySMTP(companyId);

        log.info("SMTP settings {} for company {}", saved.getId() == null ? "created" : "updated", companyId);
        return mapToResponse(saved);
    }

    @Override
    public SMTPSettingsResponse updateSMTPSettings(UpdateSMTPSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();

        SMTPSettings settings = smtpSettingsRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("SMTP settings not found for company: " + companyId));

        if (request.getProvider() != null)
            settings.setProvider(request.getProvider());
        if (StringUtils.hasText(request.getHost()))
            settings.setHost(request.getHost());
        if (request.getPort() != null)
            settings.setPort(request.getPort());
        if (StringUtils.hasText(request.getUsername()))
            settings.setUsername(request.getUsername());
        if (StringUtils.hasText(request.getPassword()))
            settings.setPassword(request.getPassword());
        if (request.getSslEnabled() != null)
            settings.setSslEnabled(request.getSslEnabled());
        if (request.getTlsEnabled() != null)
            settings.setTlsEnabled(request.getTlsEnabled());
        if (StringUtils.hasText(request.getFromEmail()))
            settings.setFromEmail(request.getFromEmail());
        if (request.getFromName() != null)
            settings.setFromName(request.getFromName());
        if (request.getConnectionTimeout() != null)
            settings.setConnectionTimeout(request.getConnectionTimeout());
        if (request.getTimeout() != null)
            settings.setTimeout(request.getTimeout());
        if (request.getWriteTimeout() != null)
            settings.setWriteTimeout(request.getWriteTimeout());
        if (request.getIsActive() != null)
            settings.setActive(request.getIsActive());

        SMTPSettings updated = smtpSettingsRepository.save(settings);
        mailService.refreshCompanySMTP(companyId);

        log.info("SMTP settings updated for company {}", companyId);
        return mapToResponse(updated);
    }

    @Override
    public SMTPSettingsResponse getSMTPSettings() {
        Long companyId = UserContext.getCurrentCompanyId();

        SMTPSettings settings = smtpSettingsRepository.findByCompanyIdAndIsActiveTrue(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active SMTP settings not found for company: " + companyId));
        return mapToResponse(settings);
    }

    @Override
    public void deleteSMTPSettings() {
        Long companyId = UserContext.getCurrentCompanyId();

        SMTPSettings settings = smtpSettingsRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("SMTP settings not found for company: " + companyId));
        settings.setActive(false);
        smtpSettingsRepository.save(settings);
        mailService.refreshCompanySMTP(companyId);
        log.info("SMTP settings deactivated for company {}", companyId);
    }

    @Override
    public boolean testConnection() {
        Long companyId = UserContext.getCurrentCompanyId();
        return mailService.testSMTPConnection(companyId);
    }

    @Override
    public void refreshCache() {
        Long companyId = UserContext.getCurrentCompanyId();
        mailService.refreshCompanySMTP(companyId);
        log.info("SMTP cache refreshed for company {}", companyId);
    }

    private SMTPSettingsResponse mapToResponse(SMTPSettings settings) {
        return SMTPSettingsResponse.builder()
                .id(settings.getId())
                .companyId(settings.getCompany().getId())
                .companyName(settings.getCompany().getCompanyName())
                .provider(settings.getProvider())
                .host(settings.getHost())
                .port(settings.getPort())
                .username(settings.getUsername())
                .fromEmail(settings.getFromEmail())
                .fromName(settings.getFromName())
                .sslEnabled(settings.isSslEnabled())
                .tlsEnabled(settings.isTlsEnabled())
                .connectionTimeout(settings.getConnectionTimeout())
                .timeout(settings.getTimeout())
                .writeTimeout(settings.getWriteTimeout())
                .isActive(settings.isActive())
                .createdAt(settings.getCreatedAt())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}