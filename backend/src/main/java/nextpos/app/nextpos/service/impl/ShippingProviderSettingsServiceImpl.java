package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateShippingProviderSettingsRequest;
import nextpos.app.nextpos.model.dto.response.ShippingProviderSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.ShippingProviderSettings;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.ShippingProviderSettingsRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
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

    @Override
    @Transactional
    public ShippingProviderSettingsResponse createShippingProviderSettings(
            CreateShippingProviderSettingsRequest request, Long createdBy) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + request.getCompanyId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + request.getWarehouseId()));

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
                .createdBy(createdBy)
                .build();

        ShippingProviderSettings saved = shippingProviderSettingsRepository.save(settings);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ShippingProviderSettingsResponse updateShippingProviderSettings(Long id, Long companyId, Long warehouseId,
            UpdateShippingProviderSettingsRequest request, Long updatedBy) {
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

        settings.setUpdatedBy(updatedBy);

        ShippingProviderSettings updated = shippingProviderSettingsRepository.save(settings);

        return mapToResponse(updated);
    }

    @Override
    public ShippingProviderSettingsResponse getShippingProviderSettings(Long id, Long companyId, Long warehouseId) {
        ShippingProviderSettings settings = shippingProviderSettingsRepository
                .findByCompanyIdAndWarehouseIdAndId(companyId, warehouseId, id)
                .orElseThrow(() -> new IllegalArgumentException("Shipping provider settings not found"));

        return mapToResponse(settings);
    }

    @Override
    public List<ShippingProviderSettingsResponse> listShippingProviderSettingsByCompany(Long companyId) {
        return shippingProviderSettingsRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShippingProviderSettingsResponse> listShippingProviderSettingsByWarehouse(Long companyId,
            Long warehouseId) {
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