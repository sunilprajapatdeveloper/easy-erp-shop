package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.ResourceNotFoundException;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.response.PaymentGatewaySettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.PaymentGatewaySettings;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.PaymentGatewaySettingsRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PaymentGatewaySettingsService;
import nextpos.app.nextpos.util.EncryptionUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentGatewaySettingsServiceImpl implements PaymentGatewaySettingsService {

    private final PaymentGatewaySettingsRepository repository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    // ---------- Company-level ----------

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public PaymentGatewaySettingsResponse createForCurrentCompany(CreatePaymentGatewaySettingRequest request) {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated with current user"));

        if (repository.existsByCompanyIdAndGatewayType(companyId, request.getGatewayType())) {
            throw new IllegalArgumentException(
                    "Gateway type " + request.getGatewayType() + " already configured for this company");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        PaymentGatewaySettings entity = buildEntity(request, company);
        PaymentGatewaySettings saved = repository.save(entity);
        log.info("Created payment gateway settings for company {}: {}", companyId, saved.getId());

        return PaymentGatewaySettingsResponse.fromEntity(saved);
    }

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @CacheEvict(value = "gatewaySettings", key = "#request.id")
    public PaymentGatewaySettingsResponse updateForCurrentCompany(UpdatePaymentGatewaySettingRequest request) {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated"));

        PaymentGatewaySettings entity = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        // Ensure the settings belong to the current user's company
        if (!entity.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Access denied to this gateway configuration");
        }

        // If gateway type is being changed, check uniqueness
        if (request.getGatewayType() != null && !request.getGatewayType().equals(entity.getGatewayType())) {
            if (repository.existsByCompanyIdAndGatewayType(companyId, request.getGatewayType())) {
                throw new IllegalArgumentException(
                        "Gateway type " + request.getGatewayType() + " already exists for this company");
            }
        }

        updateEntity(entity, request);
        PaymentGatewaySettings updated = repository.save(entity);
        log.info("Updated payment gateway settings id {} for company {}", updated.getId(), companyId);

        return PaymentGatewaySettingsResponse.fromEntity(updated);
    }

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @CacheEvict(value = "gatewaySettings", key = "#id")
    public void deleteForCurrentCompany(Long id) {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated"));

        PaymentGatewaySettings entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        if (!entity.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Access denied");
        }

        repository.delete(entity);
        log.info("Deleted payment gateway settings id {} for company {}", id, companyId);
    }

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public PaymentGatewaySettingsResponse getForCurrentCompany(Long id) {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated"));

        PaymentGatewaySettings entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        if (!entity.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Access denied");
        }

        return PaymentGatewaySettingsResponse.fromEntity(entity);
    }

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public List<PaymentGatewaySettingsResponse> getAllForCurrentCompany() {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated"));

        return repository.findByCompanyId(companyId).stream()
                .map(PaymentGatewaySettingsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public Page<PaymentGatewaySettingsResponse> getForCurrentCompanyPaginated(Pageable pageable) {
        Long companyId = UserContext.getCurrentUserCompanyId(userRepository)
                .orElseThrow(() -> new IllegalStateException("No company associated"));

        Page<PaymentGatewaySettings> page = repository.findByCompanyId(companyId, pageable);
        List<PaymentGatewaySettingsResponse> responses = page.getContent().stream()
                .map(PaymentGatewaySettingsResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    // ---------- System-level ----------
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public PaymentGatewaySettingsResponse createSystemSettings(CreatePaymentGatewaySettingRequest request) {
        if (repository.existsByCompanyIsNullAndGatewayType(request.getGatewayType())) {
            throw new IllegalArgumentException("System gateway type " + request.getGatewayType() + " already exists");
        }

        PaymentGatewaySettings entity = buildEntity(request, null);
        PaymentGatewaySettings saved = repository.save(entity);
        log.info("Created system payment gateway settings: {}", saved.getId());
        return PaymentGatewaySettingsResponse.fromEntity(saved);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @CacheEvict(value = "gatewaySettings", key = "#request.id")
    public PaymentGatewaySettingsResponse updateSystemSettings(UpdatePaymentGatewaySettingRequest request) {
        PaymentGatewaySettings entity = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        if (entity.getCompany() != null) {
            throw new IllegalArgumentException("This is not a system-level configuration");
        }

        if (request.getGatewayType() != null && !request.getGatewayType().equals(entity.getGatewayType())) {
            if (repository.existsByCompanyIsNullAndGatewayType(request.getGatewayType())) {
                throw new IllegalArgumentException(
                        "System gateway type " + request.getGatewayType() + " already exists");
            }
        }

        updateEntity(entity, request);
        PaymentGatewaySettings updated = repository.save(entity);
        log.info("Updated system payment gateway settings id {}", updated.getId());
        return PaymentGatewaySettingsResponse.fromEntity(updated);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @CacheEvict(value = "gatewaySettings", key = "#id")
    public void deleteSystemSettings(Long id) {
        PaymentGatewaySettings entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        if (entity.getCompany() != null) {
            throw new IllegalArgumentException("Not a system-level configuration");
        }

        repository.delete(entity);
        log.info("Deleted system payment gateway settings id {}", id);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public PaymentGatewaySettingsResponse getSystemSettings(Long id) {
        PaymentGatewaySettings entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentGatewaySettings not found"));

        if (entity.getCompany() != null) {
            throw new IllegalArgumentException("Not a system-level configuration");
        }

        return PaymentGatewaySettingsResponse.fromEntity(entity);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<PaymentGatewaySettingsResponse> getAllSystemSettings() {
        return repository.findByCompanyIsNull().stream()
                .map(PaymentGatewaySettingsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // ---------- Internal methods for payment processing (decrypted) ----------
    // (unchanged – they do not use user context)
    @Override
    @Cacheable(value = "gatewaySettings", key = "#companyId + '-' + #gatewayType")
    public PaymentGatewaySettings getDecryptedEntityForCompany(Long companyId, PaymentGatewayProvider gatewayType) {
        PaymentGatewaySettings entity = repository.findByCompanyIdAndGatewayType(companyId, gatewayType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment gateway settings not found for company " + companyId + " and type " + gatewayType));
        decryptSensitiveFields(entity);
        return entity;
    }

    @Override
    @Cacheable(value = "gatewaySettings", key = "'system-' + #gatewayType")
    public PaymentGatewaySettings getDecryptedSystemEntity(PaymentGatewayProvider gatewayType) {
        PaymentGatewaySettings entity = repository.findByCompanyIsNullAndGatewayType(gatewayType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "System payment gateway settings not found for type " + gatewayType));
        decryptSensitiveFields(entity);
        return entity;
    }

    // ---------- Private helpers ----------
    // (buildEntity, updateEntity, encrypt, decryptSensitiveFields – unchanged)
    private PaymentGatewaySettings buildEntity(CreatePaymentGatewaySettingRequest request, Company company) {
        return PaymentGatewaySettings.builder()
                .company(company)
                .gatewayType(request.getGatewayType())
                .publicKey(encrypt(request.getPublicKey()))
                .secretKey(encrypt(request.getSecretKey()))
                .merchantId(encrypt(request.getMerchantId()))
                .currency(request.getCurrency())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .webhookSecret(encrypt(request.getWebhookSecret()))
                .sandboxMode(request.getSandboxMode() != null ? request.getSandboxMode() : true)
                .build();
    }

    private void updateEntity(PaymentGatewaySettings entity, UpdatePaymentGatewaySettingRequest request) {
        if (request.getGatewayType() != null)
            entity.setGatewayType(request.getGatewayType());
        if (request.getPublicKey() != null)
            entity.setPublicKey(encrypt(request.getPublicKey()));
        if (request.getSecretKey() != null)
            entity.setSecretKey(encrypt(request.getSecretKey()));
        if (request.getMerchantId() != null)
            entity.setMerchantId(encrypt(request.getMerchantId()));
        if (request.getCurrency() != null)
            entity.setCurrency(request.getCurrency());
        if (request.getEnabled() != null)
            entity.setEnabled(request.getEnabled());
        if (request.getWebhookSecret() != null)
            entity.setWebhookSecret(encrypt(request.getWebhookSecret()));
        if (request.getSandboxMode() != null)
            entity.setSandboxMode(request.getSandboxMode());
    }

    private String encrypt(String raw) {
        return raw != null ? encryptionUtil.encrypt(raw) : null;
    }

    private void decryptSensitiveFields(PaymentGatewaySettings entity) {
        if (entity.getPublicKey() != null) {
            entity.setPublicKey(encryptionUtil.decrypt(entity.getPublicKey()));
        }
        if (entity.getSecretKey() != null) {
            entity.setSecretKey(encryptionUtil.decrypt(entity.getSecretKey()));
        }
        if (entity.getMerchantId() != null) {
            entity.setMerchantId(encryptionUtil.decrypt(entity.getMerchantId()));
        }
        if (entity.getWebhookSecret() != null) {
            entity.setWebhookSecret(encryptionUtil.decrypt(entity.getWebhookSecret()));
        }
    }
}