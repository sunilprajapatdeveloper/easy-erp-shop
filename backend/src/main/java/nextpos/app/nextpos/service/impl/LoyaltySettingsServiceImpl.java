package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateLoyaltySettingsRequest;
import nextpos.app.nextpos.model.dto.response.LoyaltySettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.LoyaltySettings;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.LoyaltySettingsRepository;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.LoyaltySettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoyaltySettingsServiceImpl implements LoyaltySettingsService {

    private final LoyaltySettingsRepository repository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public LoyaltySettingsResponse createLoyaltySettings(CreateLoyaltySettingsRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

        LoyaltySettings settings = LoyaltySettings.builder()
                .company(company)
                .enabled(request.getEnabled() != null ? request.getEnabled() : false)
                .programName(request.getProgramName())
                .loyaltyType(request.getLoyaltyType())
                .pointsPerCurrency(request.getPointsPerCurrency())
                .currencyPerPoint(request.getCurrencyPerPoint())
                .pointsExpiryDays(request.getPointsExpiryDays())
                .cashbackPercentage(request.getCashbackPercentage())
                .minOrderAmountForCashback(request.getMinOrderAmountForCashback())
                .tierRules(request.getTierRules())
                .minPointsToRedeem(request.getMinPointsToRedeem())
                .maxDiscountPercentage(request.getMaxDiscountPercentage())
                .extraSettings(request.getExtraSettings())
                .isActive(true)
                .createdBy(currentUserId)
                .build();

        repository.save(settings);

        return mapToResponse(settings);
    }

    @Override
    public LoyaltySettingsResponse updateLoyaltySettings(Long id, UpdateLoyaltySettingsRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

        LoyaltySettings settings = repository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new IllegalArgumentException(
                        "LoyaltySettings not found for company id: " + companyId + " and id: " + id));

        if (request.getEnabled() != null)
            settings.setEnabled(request.getEnabled());
        if (!ObjectUtils.isEmpty(request.getProgramName()))
            settings.setProgramName(request.getProgramName());
        if (!ObjectUtils.isEmpty(request.getLoyaltyType()))
            settings.setLoyaltyType(request.getLoyaltyType());
        if (request.getPointsPerCurrency() != null)
            settings.setPointsPerCurrency(request.getPointsPerCurrency());
        if (request.getCurrencyPerPoint() != null)
            settings.setCurrencyPerPoint(request.getCurrencyPerPoint());
        if (request.getPointsExpiryDays() != null)
            settings.setPointsExpiryDays(request.getPointsExpiryDays());
        if (request.getCashbackPercentage() != null)
            settings.setCashbackPercentage(request.getCashbackPercentage());
        if (request.getMinOrderAmountForCashback() != null)
            settings.setMinOrderAmountForCashback(request.getMinOrderAmountForCashback());
        if (request.getTierRules() != null)
            settings.setTierRules(request.getTierRules());
        if (request.getMinPointsToRedeem() != null)
            settings.setMinPointsToRedeem(request.getMinPointsToRedeem());
        if (request.getMaxDiscountPercentage() != null)
            settings.setMaxDiscountPercentage(request.getMaxDiscountPercentage());
        if (request.getExtraSettings() != null)
            settings.setExtraSettings(request.getExtraSettings());
        if (request.getIsActive() != null)
            settings.setIsActive(request.getIsActive());

        settings.setUpdatedBy(currentUserId);

        repository.save(settings);

        return mapToResponse(settings);
    }

    @Override
    public LoyaltySettingsResponse getLoyaltySettings(Long id) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

        LoyaltySettings settings = repository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new IllegalArgumentException(
                        "LoyaltySettings not found for company id: " + companyId + " and id: " + id));

        return mapToResponse(settings);
    }

    @Override
    public List<LoyaltySettingsResponse> listLoyaltySettings() {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

        return repository.findAllByCompany(company)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private LoyaltySettingsResponse mapToResponse(LoyaltySettings settings) {
        return LoyaltySettingsResponse.builder()
                .enabled(settings.getEnabled())
                .programName(settings.getProgramName())
                .loyaltyType(settings.getLoyaltyType())
                .pointsPerCurrency(settings.getPointsPerCurrency())
                .currencyPerPoint(settings.getCurrencyPerPoint())
                .pointsExpiryDays(settings.getPointsExpiryDays())
                .cashbackPercentage(settings.getCashbackPercentage())
                .minOrderAmountForCashback(settings.getMinOrderAmountForCashback())
                .tierRules(settings.getTierRules())
                .minPointsToRedeem(settings.getMinPointsToRedeem())
                .maxDiscountPercentage(settings.getMaxDiscountPercentage())
                .extraSettings(settings.getExtraSettings())
                .isActive(settings.getIsActive())
                .createdAt(settings.getCreatedAt())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}
