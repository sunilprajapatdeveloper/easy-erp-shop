package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.response.ExchangeRateResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.ExchangeRateRepository;
import nextpos.app.nextpos.service.interf.ExchangeRateService;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.security.access.WarehouseAccessService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseAccessService warehouseAccessService;

    @Override
    public ExchangeRateResponse createExchangeRate(CreateExchangeRateRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        if (request.getCompanyId() != null && !companyId.equals(request.getCompanyId())) {
            throw new SecurityException("Company identifier does not match authenticated tenant");
        }
        if (request.getLevel() == ExchangeRateLevel.GLOBAL) {
            throw new SecurityException("Global exchange rates are system-managed reference data");
        }

        Currency base = currencyRepository.findById(request.getBaseCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Base currency not found"));
        Currency target = currencyRepository.findById(request.getTargetCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Target currency not found"));

        Company company = null;
        Warehouse warehouse = null;

        if (request.getLevel() == ExchangeRateLevel.COMPANY || request.getLevel() == ExchangeRateLevel.WAREHOUSE) {
            company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        }

        if (request.getLevel() == ExchangeRateLevel.WAREHOUSE) {
            if (request.getWarehouseId() == null)
                throw new IllegalArgumentException("Warehouse ID required for WAREHOUSE level");
            warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
        }

        // Prevent duplicate exchange rate for same scope
        boolean exists = exchangeRateRepository
                .findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                        base, target, request.getLevel(), company, warehouse)
                .isPresent();

        if (exists) {
            throw new IllegalStateException("Exchange rate already exists for this scope");
        }

        ExchangeRate entity = ExchangeRate.builder()
                .baseCurrency(base)
                .targetCurrency(target)
                .rate(request.getRate())
                .bidRate(request.getBidRate())
                .askRate(request.getAskRate())
                .level(request.getLevel())
                .company(company)
                .warehouse(warehouse)
                .rateSource(request.getRateSource())
                .providerName(request.getProviderName())
                .providerReferenceId(request.getProviderReferenceId())
                .spreadPercentage(request.getSpreadPercentage())
                .isManualOverride(request.getIsManualOverride())
                .overrideReason(request.getOverrideReason())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .build();

        exchangeRateRepository.save(entity);

        return toResponse(entity);
    }

    @Override
    public ExchangeRateResponse updateExchangeRate(Long id, UpdateExchangeRateRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        ExchangeRate entity = exchangeRateRepository.findByIdAndCompany_Id(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found"));
        validateWarehouse(entity);

        if (request.getRate() != null)
            entity.setRate(request.getRate());
        if (request.getBidRate() != null)
            entity.setBidRate(request.getBidRate());
        if (request.getAskRate() != null)
            entity.setAskRate(request.getAskRate());
        if (request.getSpreadPercentage() != null)
            entity.setSpreadPercentage(request.getSpreadPercentage());
        if (request.getRateSource() != null)
            entity.setRateSource(request.getRateSource());
        if (request.getProviderName() != null)
            entity.setProviderName(request.getProviderName());
        if (request.getProviderReferenceId() != null)
            entity.setProviderReferenceId(request.getProviderReferenceId());
        if (request.getIsManualOverride() != null)
            entity.setIsManualOverride(request.getIsManualOverride());
        if (request.getOverrideReason() != null)
            entity.setOverrideReason(request.getOverrideReason());
        if (request.getValidFrom() != null)
            entity.setValidFrom(request.getValidFrom());
        if (request.getValidTo() != null)
            entity.setValidTo(request.getValidTo());
        if (request.getLevel() == ExchangeRateLevel.GLOBAL)
            throw new SecurityException("Global exchange rates are system-managed reference data");
        if (request.getLevel() != null)
            entity.setLevel(request.getLevel());

        // Optional: update company/warehouse scope
        if (request.getCompanyId() != null) {
            if (!companyId.equals(request.getCompanyId())) {
                throw new SecurityException("Company identifier does not match authenticated tenant");
            }
        }

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
            entity.setWarehouse(warehouse);
        }

        exchangeRateRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public ExchangeRateResponse getExchangeRate(Long id) {
        ExchangeRate entity = exchangeRateRepository.findAccessibleById(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found"));
        validateWarehouse(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteExchangeRate(Long id) {
        ExchangeRate entity = exchangeRateRepository.findByIdAndCompany_Id(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found"));
        validateWarehouse(entity);
        exchangeRateRepository.delete(entity);
    }

    @Override
    public List<ExchangeRateResponse> getAllExchangeRates() {
        var accessibleWarehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                .map(Warehouse::getId).collect(Collectors.toSet());
        return exchangeRateRepository.findTenantAndGlobal(UserContext.getCurrentCompanyId())
                .stream()
                .filter(rate -> rate.getWarehouse() == null
                        || accessibleWarehouseIds.contains(rate.getWarehouse().getId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExchangeRateResponse findExchangeRate(Long baseCurrencyId, Long targetCurrencyId, Long companyId,
            Long warehouseId) {

        Long authenticatedCompanyId = UserContext.getCurrentCompanyId();
        if (companyId != null && !authenticatedCompanyId.equals(companyId)) {
            throw new SecurityException("Company identifier does not match authenticated tenant");
        }

        Currency base = currencyRepository.findById(baseCurrencyId)
                .orElseThrow(() -> new IllegalArgumentException("Base currency not found"));
        Currency target = currencyRepository.findById(targetCurrencyId)
                .orElseThrow(() -> new IllegalArgumentException("Target currency not found"));

        Company companyTemp = null;
        Warehouse warehouseTemp = null;

        if (authenticatedCompanyId != null) {
            companyTemp = companyRepository.findById(authenticatedCompanyId)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        }

        if (warehouseId != null) {
            warehouseTemp = warehouseAccessService.requireAccessible(warehouseId);
        }

        // Make them final for lambda usage
        final Company company = companyTemp;
        final Warehouse warehouse = warehouseTemp;

        // Search in hierarchy: WAREHOUSE -> COMPANY -> GLOBAL
        ExchangeRate entity = exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                base, target, ExchangeRateLevel.WAREHOUSE, company, warehouse)
                .or(() -> exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                        base, target, ExchangeRateLevel.COMPANY, company, null))
                .or(() -> exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
                        base, target, ExchangeRateLevel.GLOBAL, null, null))
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found"));

        return toResponse(entity);
    }

    public ExchangeRateResponse toResponse(ExchangeRate entity) {
        if (entity == null)
            return null;

        return ExchangeRateResponse.builder()
                .id(entity.getId())
                .baseCurrency(ExchangeRateResponse.CurrencyInfo.builder()
                        .id(entity.getBaseCurrency().getId())
                        .code(entity.getBaseCurrency().getCode())
                        .symbol(entity.getBaseCurrency().getSymbol())
                        .name(entity.getBaseCurrency().getName())
                        .build())
                .targetCurrency(ExchangeRateResponse.CurrencyInfo.builder()
                        .id(entity.getTargetCurrency().getId())
                        .code(entity.getTargetCurrency().getCode())
                        .symbol(entity.getTargetCurrency().getSymbol())
                        .name(entity.getTargetCurrency().getName())
                        .build())
                .company(entity.getCompany() != null ? ExchangeRateResponse.CompanyInfo.builder()
                        .id(entity.getCompany().getId())
                        .name(entity.getCompany().getCompanyName())
                        .build() : null)
                .warehouse(entity.getWarehouse() != null ? ExchangeRateResponse.WarehouseInfo.builder()
                        .id(entity.getWarehouse().getId())
                        .name(entity.getWarehouse().getName())
                        .build() : null)
                .rate(entity.getRate())
                .bidRate(entity.getBidRate())
                .askRate(entity.getAskRate())
                .spreadPercentage(entity.getSpreadPercentage())
                .level(entity.getLevel())
                .isManualOverride(entity.getIsManualOverride())
                .overrideReason(entity.getOverrideReason())
                .rateSource(entity.getRateSource())
                .providerName(entity.getProviderName())
                .providerReferenceId(entity.getProviderReferenceId())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .version(entity.getVersion())
                .build();
    }

    private void validateWarehouse(ExchangeRate entity) {
        if (entity.getWarehouse() != null) {
            warehouseAccessService.requireAccessible(entity.getWarehouse().getId());
        }
    }
}
