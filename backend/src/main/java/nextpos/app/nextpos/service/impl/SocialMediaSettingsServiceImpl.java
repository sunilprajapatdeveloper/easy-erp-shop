package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSocialMediaSettingsRequest;
import nextpos.app.nextpos.model.dto.response.SocialMediaSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.SocialMediaSettings;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.SocialMediaSettingsRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.SocialMediaSettingsService;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SocialMediaSettingsServiceImpl implements SocialMediaSettingsService {

    private final SocialMediaSettingsRepository repository;
    private final CompanyRepository companyRepository;

    @Override
    public SocialMediaSettingsResponse createSocialMediaSettings(CreateSocialMediaSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));

        SocialMediaSettings entity = SocialMediaSettings.builder()
                .company(company)
                .platform(request.getPlatform())
                .profileUrl(request.getProfileUrl())
                .username(request.getUsername())
                .apiKey(request.getApiKey())
                .apiSecret(request.getApiSecret())
                .accessToken(request.getAccessToken())
                .enabled(request.getEnabled() != null ? request.getEnabled() : Boolean.TRUE)
                .providerConfig(request.getProviderConfig())
                .createdBy(currentUserId)
                .build();

        SocialMediaSettings saved = repository.save(entity);

        return mapToResponse(saved);
    }

    @Override
    public SocialMediaSettingsResponse updateSocialMediaSettings(Long id, UpdateSocialMediaSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        SocialMediaSettings entity = repository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Social media settings not found with id " + id + " for company " + companyId));

        if (request.getProfileUrl() != null)
            entity.setProfileUrl(request.getProfileUrl());
        if (request.getUsername() != null)
            entity.setUsername(request.getUsername());
        if (request.getApiKey() != null)
            entity.setApiKey(request.getApiKey());
        if (request.getApiSecret() != null)
            entity.setApiSecret(request.getApiSecret());
        if (request.getAccessToken() != null)
            entity.setAccessToken(request.getAccessToken());
        if (request.getEnabled() != null)
            entity.setEnabled(request.getEnabled());
        if (request.getProviderConfig() != null)
            entity.setProviderConfig(request.getProviderConfig());

        entity.setUpdatedBy(currentUserId);

        SocialMediaSettings saved = repository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SocialMediaSettingsResponse getSocialMediaSettings(Long id) {
        Long companyId = UserContext.getCurrentCompanyId();

        SocialMediaSettings entity = repository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Social media settings not found with id " + id + " for company " + companyId));
        return mapToResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocialMediaSettingsResponse> listSocialMediaSettings() {
        Long companyId = UserContext.getCurrentCompanyId();

        return repository.findAllByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SocialMediaSettingsResponse mapToResponse(SocialMediaSettings entity) {
        return SocialMediaSettingsResponse.builder()
                .platform(entity.getPlatform())
                .profileUrl(entity.getProfileUrl())
                .username(entity.getUsername())
                .enabled(entity.getEnabled())
                .providerConfig(entity.getProviderConfig())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedBy(entity.getUpdatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
