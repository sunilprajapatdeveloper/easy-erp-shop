package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSecuritySettingsRequest;
import nextpos.app.nextpos.model.dto.response.SecuritySettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.SecuritySettings;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.SecuritySettingsRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.SecuritySettingsService;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SecuritySettingsServiceImpl implements SecuritySettingsService {

    private final SecuritySettingsRepository securitySettingsRepository;
    private final CompanyRepository companyRepository;

    @Override
    public SecuritySettingsResponse createSecuritySettings(Long companyId, CreateSecuritySettingsRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        // Validate that the user belongs to the specified company
        if (!currentCompanyId.equals(companyId)) {
            throw new SecurityException("You can only create security settings for your own company");
        }

        log.info("Creating SecuritySettings for companyId={}", companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));

        if (securitySettingsRepository.existsByCompanyId(companyId)) {
            throw new IllegalStateException("SecuritySettings already exist for companyId " + companyId);
        }

        SecuritySettings settings = SecuritySettings.builder()
                .company(company)
                .enforceTwoFactorAuth(request.getEnforceTwoFactorAuth())
                .enforcePasswordPolicy(request.getEnforcePasswordPolicy())
                .restrictIpAccess(request.getRestrictIpAccess())
                .allowedIpRanges(request.getAllowedIpRanges())
                .maxLoginAttempts(request.getMaxLoginAttempts())
                .accountLockDurationMinutes(request.getAccountLockDurationMinutes())
                .sessionTimeoutMinutes(request.getSessionTimeoutMinutes())
                .passwordExpiryDays(request.getPasswordExpiryDays())
                .requireStrongPasswords(request.getRequireStrongPasswords())
                .allowDeviceTrust(request.getAllowDeviceTrust())
                .createdBy(currentUserId)
                .build();

        securitySettingsRepository.save(settings);
        return mapToResponse(settings);
    }

    @Override
    public SecuritySettingsResponse updateSecuritySettings(Long companyId, UpdateSecuritySettingsRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        // Validate that the user belongs to the specified company
        if (!currentCompanyId.equals(companyId)) {
            throw new SecurityException("You can only update security settings for your own company");
        }

        log.info("Updating SecuritySettings for companyId={}", companyId);

        SecuritySettings settings = securitySettingsRepository.findByCompanyId(companyId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("SecuritySettings not found for companyId " + companyId));

        settings.setEnforceTwoFactorAuth(request.getEnforceTwoFactorAuth());
        settings.setEnforcePasswordPolicy(request.getEnforcePasswordPolicy());
        settings.setRestrictIpAccess(request.getRestrictIpAccess());
        settings.setAllowedIpRanges(request.getAllowedIpRanges());
        settings.setMaxLoginAttempts(request.getMaxLoginAttempts());
        settings.setAccountLockDurationMinutes(request.getAccountLockDurationMinutes());
        settings.setSessionTimeoutMinutes(request.getSessionTimeoutMinutes());
        settings.setPasswordExpiryDays(request.getPasswordExpiryDays());
        settings.setRequireStrongPasswords(request.getRequireStrongPasswords());
        settings.setAllowDeviceTrust(request.getAllowDeviceTrust());
        settings.setUpdatedBy(currentUserId);

        securitySettingsRepository.save(settings);
        return mapToResponse(settings);
    }

    @Override
    @Transactional(readOnly = true)
    public SecuritySettingsResponse getSecuritySettings(Long companyId) {
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        // Validate that the user belongs to the specified company
        if (!currentCompanyId.equals(companyId)) {
            throw new SecurityException("You can only view security settings for your own company");
        }

        log.info("Fetching SecuritySettings for companyId={}", companyId);

        SecuritySettings settings = securitySettingsRepository.findByCompanyId(companyId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("SecuritySettings not found for companyId " + companyId));

        return mapToResponse(settings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecuritySettingsResponse> listAllSecuritySettings() {
        return securitySettingsRepository.findByCompanyId(UserContext.getCurrentCompanyId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SecuritySettingsResponse mapToResponse(SecuritySettings settings) {
        return SecuritySettingsResponse.builder()
                .enforceTwoFactorAuth(settings.getEnforceTwoFactorAuth())
                .enforcePasswordPolicy(settings.getEnforcePasswordPolicy())
                .restrictIpAccess(settings.getRestrictIpAccess())
                .allowedIpRanges(settings.getAllowedIpRanges())
                .maxLoginAttempts(settings.getMaxLoginAttempts())
                .accountLockDurationMinutes(settings.getAccountLockDurationMinutes())
                .sessionTimeoutMinutes(settings.getSessionTimeoutMinutes())
                .passwordExpiryDays(settings.getPasswordExpiryDays())
                .requireStrongPasswords(settings.getRequireStrongPasswords())
                .allowDeviceTrust(settings.getAllowDeviceTrust())
                .build();
    }
}
