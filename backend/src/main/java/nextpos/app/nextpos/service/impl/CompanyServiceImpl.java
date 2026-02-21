package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyRequest;
import nextpos.app.nextpos.model.dto.response.BrandingSettingsResponse;
import nextpos.app.nextpos.model.dto.response.CompanyCurrencyResponse;
import nextpos.app.nextpos.model.dto.response.CompanyResponse;
import nextpos.app.nextpos.model.dto.response.CompanySubscriptionResponse;
import nextpos.app.nextpos.model.dto.response.LoyaltySettingsResponse;
import nextpos.app.nextpos.model.dto.response.OnlineOrderingSettingsResponse;
import nextpos.app.nextpos.model.dto.response.SecuritySettingsResponse;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;
import nextpos.app.nextpos.model.dto.response.SocialMediaSettingsResponse;
import nextpos.app.nextpos.model.dto.response.SubscriptionPlanResponse;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;
import nextpos.app.nextpos.model.entity.BrandingSettings;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.CompanyCurrency;
import nextpos.app.nextpos.model.entity.CompanySubscription;
import nextpos.app.nextpos.model.entity.LoyaltySettings;
import nextpos.app.nextpos.model.entity.OnlineOrderingSettings;
import nextpos.app.nextpos.model.entity.SecuritySettings;
import nextpos.app.nextpos.model.entity.ShippingProviderSettings;
import nextpos.app.nextpos.model.entity.SocialMediaSettings;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.entity.TaxSetting;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.BrandingSettingsRepository;
import nextpos.app.nextpos.repository.CompanyCurrencyRepository;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.CompanySubscriptionRepository;
import nextpos.app.nextpos.repository.LoyaltySettingsRepository;
import nextpos.app.nextpos.repository.OnlineOrderingSettingsRepository;
import nextpos.app.nextpos.repository.SecuritySettingsRepository;
import nextpos.app.nextpos.repository.ShippingProviderSettingsRepository;
import nextpos.app.nextpos.repository.SocialMediaSettingsRepository;
import nextpos.app.nextpos.repository.TaxSettingRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enterprise-grade Company service implementation that orchestrates company
 * CRUD
 * and gathers per-company settings for the response DTO.
 *
 * NOTE: repository interfaces used here are expected to exist in your project.
 * If a method name differs (e.g., findByCompanyId vs findByCompany_Id), adapt
 * accordingly.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final OnlineOrderingSettingsRepository onlineOrderingSettingsRepository;
    private final SecuritySettingsRepository securitySettingsRepository;
    private final ShippingProviderSettingsRepository shippingProviderSettingsRepository;
    private final SocialMediaSettingsRepository socialMediaSettingsRepository;
    private final LoyaltySettingsRepository loyaltySettingsRepository;
    private final BrandingSettingsRepository brandingSettingsRepository;
    private final TaxSettingRepository taxSettingRepository;
    private final CompanyCurrencyRepository companyCurrencyRepository;
    private final CompanySubscriptionRepository companySubscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        log.info("Creating company '{}' (public registration)", request.getCompanyName());

        // Uniqueness checks (email and phone must be unique across companies)
        if (companyRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (companyRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .registrationNumber(request.getRegistrationNumber())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .timezone(request.getTimezone())
                .createdBy(null)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .isDeleted(false)
                .build();

        Company saved = companyRepository.save(company);

        // Seed minimal settings if requested (still using null for createdBy)
        if (Boolean.TRUE.equals(request.getEnableOnlineOrdering())) {
            OnlineOrderingSettings oos = OnlineOrderingSettings.builder()
                    .company(saved)
                    .enabled(true)
                    .createdBy(null)
                    .createdAt(LocalDateTime.now())
                    .build();
            onlineOrderingSettingsRepository.save(oos);
            saved.setOnlineOrderingSettings(oos);
        }

        if (Boolean.TRUE.equals(request.getEnableLoyaltyProgram())) {
            LoyaltySettings ls = LoyaltySettings.builder()
                    .company(saved)
                    .enabled(true)
                    .createdBy(null)
                    .createdAt(LocalDateTime.now())
                    .build();
            loyaltySettingsRepository.save(ls);
            saved.setLoyaltySettings(ls);
        }

        // Refresh to load relationships
        saved = companyRepository.findById(saved.getId()).orElse(saved);

        return mapToResponse(saved);
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request) {
        User currentUser = UserContext.getAuthenticatedUser(userRepository);
        Long updatedBy = currentUser.getId();
        log.info("Updating company id={} by user {}", companyId, updatedBy);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        // Partial updates
        if (request.getCompanyName() != null) {
            company.setCompanyName(request.getCompanyName());
        }
        if (request.getPhone() != null) {
            company.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            company.setEmail(request.getEmail());
        }
        if (request.getRegistrationNumber() != null) {
            company.setRegistrationNumber(request.getRegistrationNumber());
        }
        if (request.getCountry() != null) {
            company.setCountry(request.getCountry());
        }
        if (request.getState() != null) {
            company.setState(request.getState());
        }
        if (request.getCity() != null) {
            company.setCity(request.getCity());
        }
        if (request.getPostalCode() != null) {
            company.setPostalCode(request.getPostalCode());
        }
        if (request.getTimezone() != null) {
            company.setTimezone(request.getTimezone());
        }

        if (request.getIsActive() != null) {
            company.setIsActive(request.getIsActive());
        }
        if (request.getIsDeleted() != null) {
            company.setIsDeleted(request.getIsDeleted());
        }

        company.setUpdatedBy(updatedBy);
        company.setUpdatedAt(LocalDateTime.now());

        Company saved = companyRepository.save(company);

        // If toggles exist on request, reconcile minimal settings presence
        if (request.getEnableOnlineOrdering() != null) {
            OnlineOrderingSettings existingOos = onlineOrderingSettingsRepository.findByCompanyId(saved.getId())
                    .orElse(null);
            if (Boolean.TRUE.equals(request.getEnableOnlineOrdering())) {
                if (existingOos == null) {
                    OnlineOrderingSettings oos = OnlineOrderingSettings.builder()
                            .company(saved)
                            .enabled(true)
                            .createdBy(updatedBy)
                            .createdAt(LocalDateTime.now())
                            .build();
                    onlineOrderingSettingsRepository.save(oos);
                    saved.setOnlineOrderingSettings(oos);
                } else {
                    existingOos.setEnabled(true);
                    existingOos.setUpdatedBy(updatedBy);
                    existingOos.setUpdatedAt(LocalDateTime.now());
                    onlineOrderingSettingsRepository.save(existingOos);
                    saved.setOnlineOrderingSettings(existingOos);
                }
            } else {
                if (existingOos != null) {
                    existingOos.setEnabled(false);
                    existingOos.setUpdatedBy(updatedBy);
                    existingOos.setUpdatedAt(LocalDateTime.now());
                    onlineOrderingSettingsRepository.save(existingOos);
                    saved.setOnlineOrderingSettings(existingOos);
                }
            }
        }

        if (request.getEnableLoyaltyProgram() != null) {
            LoyaltySettings existingLs = loyaltySettingsRepository.findByCompanyId(saved.getId()).orElse(null);
            if (Boolean.TRUE.equals(request.getEnableLoyaltyProgram())) {
                if (existingLs == null) {
                    LoyaltySettings ls = LoyaltySettings.builder()
                            .company(saved)
                            .enabled(true)
                            .createdBy(updatedBy)
                            .createdAt(LocalDateTime.now())
                            .build();
                    loyaltySettingsRepository.save(ls);
                    saved.setLoyaltySettings(ls);
                } else {
                    existingLs.setEnabled(true);
                    existingLs.setUpdatedBy(updatedBy);
                    existingLs.setUpdatedAt(LocalDateTime.now());
                    loyaltySettingsRepository.save(existingLs);
                    saved.setLoyaltySettings(existingLs);
                }
            } else {
                if (existingLs != null) {
                    existingLs.setEnabled(false);
                    existingLs.setUpdatedBy(updatedBy);
                    existingLs.setUpdatedAt(LocalDateTime.now());
                    loyaltySettingsRepository.save(existingLs);
                    saved.setLoyaltySettings(existingLs);
                }
            }
        }

        // refresh
        saved = companyRepository.findById(saved.getId()).orElse(saved);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        // Load settings (same as before)
        OnlineOrderingSettings oos = onlineOrderingSettingsRepository.findByCompanyId(companyId).orElse(null);
        SecuritySettings sec = securitySettingsRepository.findByCompanyId(companyId).orElse(null);
        ShippingProviderSettings ship = shippingProviderSettingsRepository.findByCompanyId(companyId)
                .stream().findFirst().orElse(null);
        SocialMediaSettings social = socialMediaSettingsRepository.findByCompanyId(companyId).orElse(null);
        LoyaltySettings loyalty = loyaltySettingsRepository.findByCompanyId(companyId).orElse(null);
        BrandingSettings branding = brandingSettingsRepository.findByCompanyId(companyId).orElse(null);
        TaxSetting tax = taxSettingRepository.findByCompanyId(companyId).orElse(null);

        // Fetch all currencies
        List<CompanyCurrency> currencies = companyCurrencyRepository.findByCompanyId(companyId);
        company.setCurrencies(currencies);

        // Fetch active subscription
        CompanySubscription subscription = companySubscriptionRepository.findActiveSubscriptionByCompanyId(companyId)
                .orElse(null);
        company.setCompanySubscription(subscription);

        // Set other settings
        company.setOnlineOrderingSettings(oos);
        company.setSecuritySettings(sec);
        company.setShippingProviderSettings(ship);
        company.setSocialMediaSettings(social);
        company.setLoyaltySettings(loyalty);
        company.setBrandingSettings(branding);
        company.setTaxSetting(tax);

        return mapToResponse(company);
    }

    @Override
    public void deleteCompany(Long companyId) {
        User currentUser = UserContext.getAuthenticatedUser(userRepository);
        Long deletedBy = currentUser.getId();
        log.info("Soft deleting company id={} by user {}", companyId, deletedBy);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        company.setIsDeleted(true);
        company.setIsActive(false);
        company.setUpdatedBy(deletedBy);
        company.setUpdatedAt(LocalDateTime.now());

        companyRepository.save(company);
    }

    private CompanyResponse mapToResponse(Company company) {

        OnlineOrderingSettings oos = company.getOnlineOrderingSettings();
        OnlineOrderingSettingsResponse oosResp = null;
        if (oos != null) {
            oosResp = OnlineOrderingSettingsResponse.builder()
                    .id(oos.getId())
                    .companyId(oos.getCompany() != null ? oos.getCompany().getId() : null)
                    .enabled(oos.isEnabled())
                    .orderingUrl(oos.getOrderingUrl())
                    .minOrderValue(oos.getMinOrderValue())
                    .estimatedDeliveryTime(oos.getEstimatedDeliveryTime())
                    .selfPickupEnabled(oos.isSelfPickupEnabled())
                    .deliveryEnabled(oos.isDeliveryEnabled())
                    .integrationKey(oos.getIntegrationKey())
                    .customerNotes(oos.getCustomerNotes())
                    .createdBy(oos.getCreatedBy())
                    .createdAt(oos.getCreatedAt())
                    .updatedBy(oos.getUpdatedBy())
                    .updatedAt(oos.getUpdatedAt())
                    .build();
        }

        SecuritySettings sec = company.getSecuritySettings();
        SecuritySettingsResponse secResp = null;
        if (sec != null) {
            secResp = SecuritySettingsResponse.builder()
                    .enforceTwoFactorAuth(sec.getEnforceTwoFactorAuth())
                    .enforcePasswordPolicy(sec.getEnforcePasswordPolicy())
                    .restrictIpAccess(sec.getRestrictIpAccess())
                    .allowedIpRanges(sec.getAllowedIpRanges())
                    .maxLoginAttempts(sec.getMaxLoginAttempts())
                    .accountLockDurationMinutes(sec.getAccountLockDurationMinutes())
                    .sessionTimeoutMinutes(sec.getSessionTimeoutMinutes())
                    .passwordExpiryDays(sec.getPasswordExpiryDays())
                    .requireStrongPasswords(sec.getRequireStrongPasswords())
                    .allowDeviceTrust(sec.getAllowDeviceTrust())
                    .build();
        }

        ShippingProviderSettings ship = company.getShippingProviderSettings();
        ShippingProviderSettingsResponse shipResp = null;
        if (ship != null) {
            shipResp = ShippingProviderSettingsResponse.builder()
                    .id(ship.getId())
                    .companyId(ship.getCompany() != null ? ship.getCompany().getId() : null)
                    .warehouseId(ship.getWarehouse() != null ? ship.getWarehouse().getId() : null)
                    .providerName(ship.getProviderName())
                    .accountId(ship.getAccountId())
                    .apiKey(ship.getApiKey())
                    .apiSecret(ship.getApiSecret())
                    .apiEndpoint(ship.getApiEndpoint())
                    .enabled(ship.getEnabled())
                    .serviceRegions(ship.getServiceRegions())
                    .providerConfig(ship.getProviderConfig())
                    .createdBy(ship.getCreatedBy())
                    .createdAt(ship.getCreatedAt())
                    .updatedBy(ship.getUpdatedBy())
                    .updatedAt(ship.getUpdatedAt())
                    .build();
        }

        SocialMediaSettings social = company.getSocialMediaSettings();
        SocialMediaSettingsResponse socialResp = null;
        if (social != null) {
            socialResp = SocialMediaSettingsResponse.builder()
                    .platform(social.getPlatform())
                    .profileUrl(social.getProfileUrl())
                    .username(social.getUsername())
                    .enabled(social.getEnabled())
                    .providerConfig(social.getProviderConfig())
                    .createdBy(social.getCreatedBy())
                    .createdAt(social.getCreatedAt())
                    .updatedBy(social.getUpdatedBy())
                    .updatedAt(social.getUpdatedAt())
                    .build();
        }

        LoyaltySettings loyalty = company.getLoyaltySettings();
        LoyaltySettingsResponse loyaltyResp = null;
        if (loyalty != null) {
            loyaltyResp = LoyaltySettingsResponse.builder()
                    .enabled(loyalty.getEnabled())
                    .programName(loyalty.getProgramName())
                    .loyaltyType(loyalty.getLoyaltyType())
                    .pointsPerCurrency(loyalty.getPointsPerCurrency())
                    .currencyPerPoint(loyalty.getCurrencyPerPoint())
                    .pointsExpiryDays(loyalty.getPointsExpiryDays())
                    .cashbackPercentage(loyalty.getCashbackPercentage())
                    .minOrderAmountForCashback(loyalty.getMinOrderAmountForCashback())
                    .tierRules(loyalty.getTierRules())
                    .minPointsToRedeem(loyalty.getMinPointsToRedeem())
                    .maxDiscountPercentage(loyalty.getMaxDiscountPercentage())
                    .extraSettings(loyalty.getExtraSettings())
                    .isActive(loyalty.getIsActive())
                    .createdAt(loyalty.getCreatedAt())
                    .updatedAt(loyalty.getUpdatedAt())
                    .build();
        }

        BrandingSettings branding = company.getBrandingSettings();
        BrandingSettingsResponse brandingResp = null;
        if (branding != null) {
            brandingResp = BrandingSettingsResponse.builder()
                    .logoLight(branding.getLogoLight())
                    .logoDark(branding.getLogoDark())
                    .favicon(branding.getFavicon())
                    .primaryColor(branding.getPrimaryColor())
                    .secondaryColor(branding.getSecondaryColor())
                    .accentColor(branding.getAccentColor())
                    .backgroundColor(branding.getBackgroundColor())
                    .textColor(branding.getTextColor())
                    .fontFamily(branding.getFontFamily())
                    .fontSize(branding.getFontSize())
                    .customTheme(branding.getCustomTheme())
                    .isActive(branding.getIsActive())
                    .createdAt(branding.getCreatedAt())
                    .updatedAt(branding.getUpdatedAt())
                    .build();
        }

        TaxSetting tax = company.getTaxSetting();
        TaxSettingResponse taxResp = null;
        if (tax != null) {
            taxResp = TaxSettingResponse.builder()
                    .taxType(tax.getTaxType())
                    .name(tax.getName())
                    .rate(tax.getRate())
                    .calculationType(tax.getCalculationType())
                    .inclusiveType(tax.getInclusiveType())
                    .active(tax.isActive())
                    .regionCode(tax.getRegionCode())
                    .description(tax.getDescription())
                    .warehouseId(tax.getWarehouse() != null ? tax.getWarehouse().getId() : null)
                    .build();
        }

        CompanyCurrency defaultCurrency = company.getDefaultCurrency(); // helper method in Company entity
        CompanyCurrencyResponse currencyResp = null;
        if (defaultCurrency != null) {
            currencyResp = CompanyCurrencyResponse.builder()
                    .id(defaultCurrency.getId())
                    .currencyId(defaultCurrency.getCurrency() != null ? defaultCurrency.getCurrency().getId() : null)
                    .currencyCode(
                            defaultCurrency.getCurrency() != null ? defaultCurrency.getCurrency().getCode() : null)
                    .currencyName(
                            defaultCurrency.getCurrency() != null ? defaultCurrency.getCurrency().getName() : null)
                    .symbol(defaultCurrency.getCurrency() != null ? defaultCurrency.getCurrency().getSymbol() : null)
                    .decimalPlaces(defaultCurrency.getDecimalPlaces())
                    .defaultCurrency(defaultCurrency.isDefaultCurrency())
                    .status(defaultCurrency.getStatus())
                    .companyId(defaultCurrency.getCompany() != null ? defaultCurrency.getCompany().getId() : null)
                    .build();
        }

        // Company Subscription
        CompanySubscription subscription = company.getCompanySubscription();
        CompanySubscriptionResponse subscriptionResp = subscription != null ? mapToSubscriptionResponse(subscription)
                : null;

        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .phone(company.getPhone())
                .email(company.getEmail())
                .registrationNumber(company.getRegistrationNumber())
                .country(company.getCountry())
                .state(company.getState())
                .city(company.getCity())
                .postalCode(company.getPostalCode())
                .timezone(company.getTimezone())
                .createdBy(company.getCreatedBy())
                .createdAt(company.getCreatedAt())
                .updatedBy(company.getUpdatedBy())
                .updatedAt(company.getUpdatedAt())
                .isActive(company.getIsActive())
                .isDeleted(company.getIsDeleted())
                .onlineOrderingSettings(oosResp)
                .securitySettings(secResp)
                .shippingProviderSettings(shipResp)
                .socialMediaSettings(socialResp)
                .loyaltySettings(loyaltyResp)
                .brandingSettings(brandingResp)
                .taxSetting(taxResp)
                .currencySetting(currencyResp)
                .subscription(subscriptionResp)
                .build();
    }

    /**
     * Helper method to convert CompanySubscription entity to response DTO.
     */
    private CompanySubscriptionResponse mapToSubscriptionResponse(CompanySubscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionPlan subscriptionPlan = subscription.getSubscriptionPlan();
        SubscriptionPlanResponse planResp = null;
        if (subscriptionPlan != null) {
            planResp = SubscriptionPlanResponse.builder()
                    .id(subscriptionPlan.getId())
                    .name(subscriptionPlan.getName())
                    .description(subscriptionPlan.getDescription())
                    .price(subscriptionPlan.getPrice())
                    .currency(subscriptionPlan.getCurrency())
                    .billingCycle(subscriptionPlan.getBillingCycle())
                    .trialAvailable(subscriptionPlan.isTrialAvailable())
                    .trialDays(subscriptionPlan.getTrialDays())
                    .maxUsers(subscriptionPlan.getMaxUsers())
                    .maxBranches(subscriptionPlan.getMaxBranches())
                    .features(subscriptionPlan.getFeatures())
                    .availableRegions(subscriptionPlan.getAvailableRegions())
                    .status(subscriptionPlan.getStatus())
                    .isDeleted(subscriptionPlan.isDeleted())
                    .createdBy(subscriptionPlan.getCreatedBy())
                    .updatedBy(subscriptionPlan.getUpdatedBy())
                    .createdAt(subscriptionPlan.getCreatedAt())
                    .updatedAt(subscriptionPlan.getUpdatedAt())
                    .version(subscriptionPlan.getVersion())
                    .build();
        }

        return CompanySubscriptionResponse.builder()
                .id(subscription.getId())
                .companyId(subscription.getCompany() != null ? subscription.getCompany().getId() : null)
                .subscriptionPlanId(subscriptionPlan != null ? subscriptionPlan.getId() : null)
                .plan(planResp)
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .trialActive(subscription.isTrialActive())
                .trialEndDate(subscription.getTrialEndDate())
                .autoRenew(subscription.isAutoRenew())
                .nextBillingDate(subscription.getNextBillingDate())
                .createdBy(subscription.getCreatedBy())
                .createdAt(subscription.getCreatedAt())
                .updatedBy(subscription.getUpdatedBy())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }
}
