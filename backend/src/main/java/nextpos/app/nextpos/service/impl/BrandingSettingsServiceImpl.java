package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateBrandingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.BrandingSettingsResponse;
import nextpos.app.nextpos.model.entity.BrandingSettings;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.repository.BrandingSettingsRepository;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.service.interf.BrandingSettingsService;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BrandingSettingsServiceImpl implements BrandingSettingsService {

    private final BrandingSettingsRepository brandingSettingsRepository;
    private final CompanyRepository companyRepository;

    @Override
    public BrandingSettingsResponse createBrandingSettings(CreateBrandingSettingsRequest request, Long companyId,
            Long createdBy) {
        log.info("Creating BrandingSettings for companyId={}", companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        BrandingSettings brandingSettings = BrandingSettings.builder()
                .company(company)
                .logoLight(request.getLogoLight())
                .logoDark(request.getLogoDark())
                .favicon(request.getFavicon())
                .primaryColor(request.getPrimaryColor())
                .secondaryColor(request.getSecondaryColor())
                .accentColor(request.getAccentColor())
                .backgroundColor(request.getBackgroundColor())
                .textColor(request.getTextColor())
                .fontFamily(request.getFontFamily())
                .fontSize(request.getFontSize())
                .customTheme(request.getCustomTheme())
                .isActive(request.getIsActive())
                .createdBy(createdBy)
                .build();

        BrandingSettings saved = brandingSettingsRepository.save(brandingSettings);
        return mapToResponse(saved);
    }

    @Override
    public BrandingSettingsResponse updateBrandingSettings(Long id, Long companyId,
            UpdateBrandingSettingsRequest request, Long updatedBy) {
        log.info("Updating BrandingSettings id={} for companyId={}", id, companyId);

        BrandingSettings brandingSettings = brandingSettingsRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("BrandingSettings not found with id: " + id));

        if (request.getLogoLight() != null)
            brandingSettings.setLogoLight(request.getLogoLight());
        if (request.getLogoDark() != null)
            brandingSettings.setLogoDark(request.getLogoDark());
        if (request.getFavicon() != null)
            brandingSettings.setFavicon(request.getFavicon());

        if (request.getPrimaryColor() != null)
            brandingSettings.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null)
            brandingSettings.setSecondaryColor(request.getSecondaryColor());
        if (request.getAccentColor() != null)
            brandingSettings.setAccentColor(request.getAccentColor());
        if (request.getBackgroundColor() != null)
            brandingSettings.setBackgroundColor(request.getBackgroundColor());
        if (request.getTextColor() != null)
            brandingSettings.setTextColor(request.getTextColor());

        if (request.getFontFamily() != null)
            brandingSettings.setFontFamily(request.getFontFamily());
        if (request.getFontSize() != null)
            brandingSettings.setFontSize(request.getFontSize());

        if (request.getCustomTheme() != null)
            brandingSettings.setCustomTheme(request.getCustomTheme());
        if (request.getIsActive() != null)
            brandingSettings.setIsActive(request.getIsActive());

        brandingSettings.setUpdatedBy(updatedBy);

        BrandingSettings updated = brandingSettingsRepository.save(brandingSettings);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandingSettingsResponse getBrandingSettings(Long id, Long companyId) {
        log.info("Fetching BrandingSettings id={} for companyId={}", id, companyId);

        BrandingSettings brandingSettings = brandingSettingsRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("BrandingSettings not found with id: " + id));

        return mapToResponse(brandingSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandingSettingsResponse> listBrandingSettings(Long companyId) {
        log.info("Listing BrandingSettings for companyId={}", companyId);

        return brandingSettingsRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBrandingSettings(Long id, Long companyId) {
        log.info("Deleting BrandingSettings id={} for companyId={}", id, companyId);

        BrandingSettings brandingSettings = brandingSettingsRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("BrandingSettings not found with id: " + id));

        brandingSettingsRepository.delete(brandingSettings);
    }

    private BrandingSettingsResponse mapToResponse(BrandingSettings brandingSettings) {
        return BrandingSettingsResponse.builder()
                .logoLight(brandingSettings.getLogoLight())
                .logoDark(brandingSettings.getLogoDark())
                .favicon(brandingSettings.getFavicon())
                .primaryColor(brandingSettings.getPrimaryColor())
                .secondaryColor(brandingSettings.getSecondaryColor())
                .accentColor(brandingSettings.getAccentColor())
                .backgroundColor(brandingSettings.getBackgroundColor())
                .textColor(brandingSettings.getTextColor())
                .fontFamily(brandingSettings.getFontFamily())
                .fontSize(brandingSettings.getFontSize())
                .customTheme(brandingSettings.getCustomTheme())
                .isActive(brandingSettings.getIsActive())
                .createdAt(brandingSettings.getCreatedAt())
                .updatedAt(brandingSettings.getUpdatedAt())
                .build();
    }
}