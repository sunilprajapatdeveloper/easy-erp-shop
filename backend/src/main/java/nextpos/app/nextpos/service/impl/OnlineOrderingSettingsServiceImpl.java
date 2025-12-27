package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateOnlineOrderingSettingsRequest;
import nextpos.app.nextpos.model.dto.response.OnlineOrderingSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.OnlineOrderingSettings;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.OnlineOrderingSettingsRepository;
import nextpos.app.nextpos.service.interf.OnlineOrderingSettingsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OnlineOrderingSettingsServiceImpl implements OnlineOrderingSettingsService {

        private final OnlineOrderingSettingsRepository settingsRepository;
        private final CompanyRepository companyRepository;

        @Override
        public OnlineOrderingSettingsResponse createOnlineOrderingSettings(CreateOnlineOrderingSettingsRequest request,
                        Long createdBy) {
                Company company = companyRepository.findById(request.getCompanyId())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Company not found with id: " + request.getCompanyId()));

                if (settingsRepository.existsByCompanyAndIsDeletedFalse(company)) {
                        throw new IllegalStateException(
                                        "OnlineOrderingSettings already exists for company: " + request.getCompanyId());
                }

                OnlineOrderingSettings settings = OnlineOrderingSettings.builder()
                                .company(company)
                                .enabled(request.getEnabled())
                                .orderingUrl(request.getOrderingUrl())
                                .minOrderValue(request.getMinOrderValue())
                                .estimatedDeliveryTime(request.getEstimatedDeliveryTime())
                                .selfPickupEnabled(request.getSelfPickupEnabled())
                                .deliveryEnabled(request.getDeliveryEnabled())
                                .integrationKey(request.getIntegrationKey())
                                .customerNotes(request.getCustomerNotes())
                                .integrationConfig(request.getIntegrationConfig())
                                .createdBy(createdBy)
                                .build();

                OnlineOrderingSettings saved = settingsRepository.save(settings);
                log.info("Created OnlineOrderingSettings for companyId={}", company.getId());
                return mapToResponse(saved);
        }

        @Override
        public OnlineOrderingSettingsResponse updateOnlineOrderingSettings(UpdateOnlineOrderingSettingsRequest request,
                        Long updatedBy) {
                Company company = companyRepository.findById(request.getCompanyId())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Company not found with id: " + request.getCompanyId()));

                OnlineOrderingSettings settings = settingsRepository.findByCompanyAndIsDeletedFalse(company)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "OnlineOrderingSettings not found for companyId="
                                                                + request.getCompanyId()));

                settings.setEnabled(request.getEnabled());
                settings.setOrderingUrl(request.getOrderingUrl());
                settings.setMinOrderValue(request.getMinOrderValue());
                settings.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
                settings.setSelfPickupEnabled(request.getSelfPickupEnabled());
                settings.setDeliveryEnabled(request.getDeliveryEnabled());
                settings.setIntegrationKey(request.getIntegrationKey());
                settings.setCustomerNotes(request.getCustomerNotes());
                settings.setIntegrationConfig(request.getIntegrationConfig());
                settings.setUpdatedBy(updatedBy);

                OnlineOrderingSettings saved = settingsRepository.save(settings);
                log.info("Updated OnlineOrderingSettings for companyId={}", company.getId());
                return mapToResponse(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public OnlineOrderingSettingsResponse getOnlineOrderingSettings(Long companyId) {
                Company company = companyRepository.findById(companyId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Company not found with id: " + companyId));

                OnlineOrderingSettings settings = settingsRepository.findByCompanyAndIsDeletedFalse(company)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "OnlineOrderingSettings not found for companyId=" + companyId));

                return mapToResponse(settings);
        }

        @Override
        public void deleteOnlineOrderingSettings(Long companyId, Long deletedBy) {
                Company company = companyRepository.findById(companyId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Company not found with id: " + companyId));

                OnlineOrderingSettings settings = settingsRepository.findByCompanyAndIsDeletedFalse(company)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "OnlineOrderingSettings not found for companyId=" + companyId));

                settings.setDeleted(true);
                settings.setUpdatedBy(deletedBy);
                settingsRepository.save(settings);
                log.info("Soft-deleted OnlineOrderingSettings for companyId={}", companyId);
        }

        // Mapper: Entity -> Response DTO
        private OnlineOrderingSettingsResponse mapToResponse(OnlineOrderingSettings settings) {
                return OnlineOrderingSettingsResponse.builder()
                                .enabled(settings.isEnabled())
                                .orderingUrl(settings.getOrderingUrl())
                                .minOrderValue(settings.getMinOrderValue())
                                .estimatedDeliveryTime(settings.getEstimatedDeliveryTime())
                                .selfPickupEnabled(settings.isSelfPickupEnabled())
                                .deliveryEnabled(settings.isDeliveryEnabled())
                                .integrationKey(settings.getIntegrationKey())
                                .customerNotes(settings.getCustomerNotes())
                                .integrationConfig(settings.getIntegrationConfig())
                                .build();
        }
}