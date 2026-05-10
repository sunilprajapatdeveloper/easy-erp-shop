package nextpos.app.nextpos.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePOSGeneralSettingsRequest;
import nextpos.app.nextpos.model.dto.response.POSGeneralSettingsResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.model.entity.POSGeneralSettings;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.entity.WarehouseCurrency;
import nextpos.app.nextpos.model.enums.CurrencyStatus;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.repository.POSGeneralSettingsRepository;
import nextpos.app.nextpos.repository.WarehouseCurrencyRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.POSGeneralSettingsService;

@Service
@RequiredArgsConstructor
public class POSGeneralSettingsServiceImpl implements POSGeneralSettingsService {

    private final POSGeneralSettingsRepository posRepo;
    private final WarehouseRepository warehouseRepo;
    private final CompanyRepository companyRepo;
    private final CustomerRepository customerRepo;
    private final WarehouseCurrencyRepository warehouseCurrencyRepo;

    @Override
    @Transactional
    public POSGeneralSettingsResponse createPOSSettings(Long warehouseId, CreatePOSGeneralSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        // Validate company
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company not found: " + companyId));

        // Validate warehouse and that it belongs to company
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found: " + warehouseId));
        if (warehouse.getCompanyId() == null || !companyId.equals(warehouse.getCompanyId())) {
            throw new IllegalArgumentException("Warehouse does not belong to the provided company");
        }

        // Validate currency: must exist, belong to the warehouse, and be active
        WarehouseCurrency currency = warehouseCurrencyRepo.findById(request.getDefaultCurrencyId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Currency not found: " + request.getDefaultCurrencyId()));
        if (!currency.getWarehouse().getId().equals(warehouseId)) {
            throw new IllegalArgumentException("Currency does not belong to the specified warehouse");
        }
        if (currency.getStatus() != CurrencyStatus.ACTIVE) {
            throw new IllegalStateException("Currency is not active");
        }

        // Validate optional customer
        Customer customer = null;
        if (request.getDefaultCustomerId() != null) {
            customer = customerRepo.findById(request.getDefaultCustomerId())
                    .orElseThrow(
                            () -> new NoSuchElementException("Customer not found: " + request.getDefaultCustomerId()));
        }

        // If settings already exist for this warehouse, throw or update? We'll update
        // existing
        Optional<POSGeneralSettings> existingOpt = posRepo.findByWarehouse(warehouse);
        POSGeneralSettings settings = existingOpt.orElse(POSGeneralSettings.builder()
                .warehouse(warehouse)
                .company(company)
                .build());

        settings.setDefaultCustomer(customer);
        settings.setDefaultCurrency(currency);
        settings.setDefaultPaymentMethod(request.getDefaultPaymentMethod());
        settings.setDefaultTaxInclusive(request.isDefaultTaxInclusive());
        settings.setCreatedBy(userId);

        POSGeneralSettings saved = posRepo.save(settings);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public POSGeneralSettingsResponse getByWarehouse(Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();

        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found: " + warehouseId));

        // Ensure warehouse belongs to company
        if (warehouse.getCompanyId() == null || !companyId.equals(warehouse.getCompanyId())) {
            throw new IllegalArgumentException("Warehouse does not belong to the provided company");
        }

        POSGeneralSettings settings = posRepo.findByWarehouse(warehouse)
                .orElseThrow(() -> new NoSuchElementException("POS settings not found for warehouse: " + warehouseId));

        return mapToResponse(settings);
    }

    @Override
    @Transactional
    public POSGeneralSettingsResponse updatePOSSettings(Long warehouseId, Long id,
            UpdatePOSGeneralSettingsRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        // Find setting by id and ensure scope matches
        POSGeneralSettings settings = posRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("POS settings not found: " + id));

        if (settings.getCompany() == null || !companyId.equals(settings.getCompany().getId())) {
            throw new IllegalArgumentException("POS settings do not belong to the provided company");
        }

        if (settings.getWarehouse() == null || !warehouseId.equals(settings.getWarehouse().getId())) {
            throw new IllegalArgumentException("POS settings do not belong to the provided warehouse");
        }

        // Update customer if present
        if (request.getDefaultCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getDefaultCustomerId())
                    .orElseThrow(
                            () -> new NoSuchElementException("Customer not found: " + request.getDefaultCustomerId()));
            settings.setDefaultCustomer(customer);
        }

        // Update currency if present
        if (request.getDefaultCurrencyId() != null) {
            WarehouseCurrency currency = warehouseCurrencyRepo.findById(request.getDefaultCurrencyId())
                    .orElseThrow(
                            () -> new NoSuchElementException("Currency not found: " + request.getDefaultCurrencyId()));
            // Also verify that the currency belongs to the warehouse and is active
            if (!currency.getWarehouse().getId().equals(warehouseId)) {
                throw new IllegalArgumentException("Currency does not belong to the specified warehouse");
            }
            if (currency.getStatus() != CurrencyStatus.ACTIVE) {
                throw new IllegalStateException("Currency is not active");
            }
            settings.setDefaultCurrency(currency);
        }

        if (request.getDefaultPaymentMethod() != null) {
            settings.setDefaultPaymentMethod(request.getDefaultPaymentMethod());
        }

        if (request.getDefaultTaxInclusive() != null) {
            settings.setDefaultTaxInclusive(request.getDefaultTaxInclusive());
        }

        settings.setUpdatedBy(userId);

        POSGeneralSettings saved = posRepo.save(settings);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deletePOSSettings(Long warehouseId, Long id) {
        Long companyId = UserContext.getCurrentCompanyId();

        POSGeneralSettings settings = posRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("POS settings not found: " + id));

        if (settings.getCompany() == null || !companyId.equals(settings.getCompany().getId())) {
            throw new IllegalArgumentException("POS settings do not belong to the provided company");
        }

        if (settings.getWarehouse() == null || !warehouseId.equals(settings.getWarehouse().getId())) {
            throw new IllegalArgumentException("POS settings do not belong to the provided warehouse");
        }

        posRepo.delete(settings);
    }

    private POSGeneralSettingsResponse mapToResponse(POSGeneralSettings posGeneralSettings) {
        Long warehouseId = posGeneralSettings.getWarehouse() != null ? posGeneralSettings.getWarehouse().getId() : null;
        String warehouseName = null;
        if (posGeneralSettings.getWarehouse() != null) {
            warehouseName = posGeneralSettings.getWarehouse().getName();
        }

        Long companyId = posGeneralSettings.getCompany() != null ? posGeneralSettings.getCompany().getId() : null;
        String companyName = null;
        if (posGeneralSettings.getCompany() != null) {
            companyName = posGeneralSettings.getCompany().getCompanyName();
        }

        Long defaultCustomerId = null;
        String defaultCustomerName = null;
        if (posGeneralSettings.getDefaultCustomer() != null) {
            defaultCustomerId = posGeneralSettings.getDefaultCustomer().getId();
            defaultCustomerName = posGeneralSettings.getDefaultCustomer().getName();
        }

        Long defaultCurrencyId = null;
        String defaultCurrencyCode = null;
        String defaultCurrencySymbol = null;
        if (posGeneralSettings.getDefaultCurrency() != null) {
            defaultCurrencyId = posGeneralSettings.getDefaultCurrency().getId();
            try {
                if (posGeneralSettings.getDefaultCurrency().getCurrency() != null) {
                    defaultCurrencyCode = posGeneralSettings.getDefaultCurrency().getCurrency().getCode();
                    defaultCurrencySymbol = posGeneralSettings.getDefaultCurrency().getCurrency().getSymbol();
                } else {
                    defaultCurrencyCode = posGeneralSettings.getDefaultCurrency().getCurrency().getCode();
                    defaultCurrencySymbol = posGeneralSettings.getDefaultCurrency().getCurrency().getSymbol();
                }
            } catch (Exception ex) {
                // Swallow
            }
        }

        return POSGeneralSettingsResponse.builder()
                .id(posGeneralSettings.getId())
                .warehouseId(warehouseId)
                .warehouseName(warehouseName)
                .companyId(companyId)
                .companyName(companyName)
                .defaultCustomerId(defaultCustomerId)
                .defaultCustomerName(defaultCustomerName)
                .defaultCurrencyId(defaultCurrencyId)
                .defaultCurrencyCode(defaultCurrencyCode)
                .defaultCurrencySymbol(defaultCurrencySymbol)
                .defaultPaymentMethod(posGeneralSettings.getDefaultPaymentMethod())
                .defaultTaxInclusive(posGeneralSettings.isDefaultTaxInclusive())
                .createdBy(posGeneralSettings.getCreatedBy())
                .createdAt(posGeneralSettings.getCreatedAt())
                .updatedBy(posGeneralSettings.getUpdatedBy())
                .updatedAt(posGeneralSettings.getUpdatedAt())
                .build();
    }
}
