package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.ShippingProviderSettings;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.ShippingProviderSettingsRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ShippingProviderSettingsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingProviderSettingsServiceImpl implements ShippingProviderSettingsService {

    private final ShippingProviderSettingsRepository shippingProviderSettingsRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;   // for UserContext

    @Override
    @Transactional
    public ShippingProviderSettingsResponse createShippingProviderSettings(
            CreateShippingProviderSettingsRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + request.getWarehouseId()));

        // Ensure warehouse belongs to the user's company
        if (!warehouse.getCompanyId().equals(companyId)) {
            throw new SecurityException("Warehouse does not belong to your company");
        }

        ShippingProviderSettings settings = ShippingProviderSettings.builder()
                .company(company)
                .warehouse(warehouse)
                .providerName(request.getProviderName())
                .accountId(request.getAccountId())
                .apiKey(request.getApiKey())
                .apiSecret(request.getApiSecret())
                .apiEndpoint(request.getApiEndpoint())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .serviceRegions(request.getServiceRegions())
                .providerConfig(request.getProviderConfig())
                .createdBy(currentUserId)
                .build();

        ShippingProviderSettings saved = shippingProviderSettingsRepository.save(settings);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ShippingProviderSettingsResponse updateShippingProviderSettings(Long id, Long warehouseId,
            UpdateShippingProviderSettingsRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        ShippingProviderSettings settings = shippingProviderSettingsRepository
                .findByCompanyIdAndWarehouseIdAndId(companyId, warehouseId, id)
                .orElseThrow(() -> new IllegalArgumentException("Shipping provider settings not found"));

        if (request.getProviderName() != null)
            settings.setProviderName(request.getProviderName());
        if (request.getAccountId() != null)
            settings.setAccountId(request.getAccountId());
        if (request.getApiKey() != null)
            settings.setApiKey(request.getApiKey());
        if (request.getApiSecret() != null)
            settings.setApiSecret(request.getApiSecret());
        if (request.getApiEndpoint() != null)
            settings.setApiEndpoint(request.getApiEndpoint());
        if (request.getEnabled() != null)
            settings.setEnabled(request.getEnabled());
        if (request.getServiceRegions() != null)
            settings.setServiceRegions(request.getServiceRegions());
        if (request.getProviderConfig() != null)
            settings.setProviderConfig(request.getProviderConfig());

        settings.setUpdatedBy(currentUserId);

        ShippingProviderSettings updated = shippingProviderSettingsRepository.save(settings);

        return mapToResponse(updated);
    }

    @Override
    public ShippingProviderSettingsResponse getShippingProviderSettings(Long id, Long warehouseId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        ShippingProviderSettings settings = shippingProviderSettingsRepository
                .findByCompanyIdAndWarehouseIdAndId(companyId, warehouseId, id)
                .orElseThrow(() -> new IllegalArgumentException("Shipping provider settings not found"));

        return mapToResponse(settings);
    }

    @Override
    public List<ShippingProviderSettingsResponse> listShippingProviderSettingsByCompany() {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        return shippingProviderSettingsRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShippingProviderSettingsResponse> listShippingProviderSettingsByWarehouse(Long warehouseId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        return shippingProviderSettingsRepository.findByCompanyIdAndWarehouseId(companyId, warehouseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ShippingProviderSettingsResponse mapToResponse(ShippingProviderSettings settings) {
        return ShippingProviderSettingsResponse.builder()
                .id(settings.getId())
                .companyId(settings.getCompany().getId())
                .warehouseId(settings.getWarehouse().getId())
                .providerName(settings.getProviderName())
                .accountId(settings.getAccountId())
                .apiKey(settings.getApiKey())
                .apiSecret(settings.getApiSecret())
                .apiEndpoint(settings.getApiEndpoint())
                .enabled(settings.getEnabled())
                .serviceRegions(settings.getServiceRegions())
                .providerConfig(settings.getProviderConfig())
                .createdBy(settings.getCreatedBy())
                .createdAt(settings.getCreatedAt())
                .updatedBy(settings.getUpdatedBy())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}