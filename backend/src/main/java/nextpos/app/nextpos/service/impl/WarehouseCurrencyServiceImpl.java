package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseCurrencyResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.entity.WarehouseCurrency;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.WarehouseCurrencyRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.service.interf.WarehouseCurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WarehouseCurrencyServiceImpl implements WarehouseCurrencyService {

    private final WarehouseCurrencyRepository warehouseCurrencyRepository;
    private final CurrencyRepository currencyRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public WarehouseCurrencyResponse createWarehouseCurrency(Long companyId, Long warehouseId,
            CreateWarehouseCurrencyRequest request) {
        log.info("Creating warehouse currency for companyId={} warehouseId={} currencyId={}",
                companyId, warehouseId, request.getCurrencyId());

        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        Warehouse warehouse = warehouseRepository.findByIdAndCompanyIdAndIsDeletedFalse(warehouseId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + warehouseId));

        // Ensure only one default currency per warehouse
        if (request.isDefaultCurrency()) {
            boolean exists = warehouseCurrencyRepository
                    .existsByCompany_IdAndWarehouse_IdAndDefaultCurrencyTrue(companyId, warehouseId);
            if (exists) {
                throw new IllegalStateException("Default currency already exists for warehouse " + warehouseId);
            }
        }

        WarehouseCurrency wc = WarehouseCurrency.builder()
                .currency(currency)
                .decimalPlaces(request.getDecimalPlaces() != null ? request.getDecimalPlaces() : 2)
                .defaultCurrency(request.isDefaultCurrency())
                .status(request.getStatus())
                .company(company)
                .warehouse(warehouse)
                .build();

        WarehouseCurrency saved = warehouseCurrencyRepository.save(wc);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseCurrencyResponse getWarehouseCurrency(Long id, Long companyId, Long warehouseId) {
        WarehouseCurrency wc = warehouseCurrencyRepository
                .findByIdAndCompanyIdAndWarehouseId(id, companyId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "WarehouseCurrency not found for id " + id + " company " + companyId + " warehouse "
                                + warehouseId));
        return mapToResponse(wc);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseCurrencyResponse getDefaultWarehouseCurrency(Long companyId, Long warehouseId) {
        WarehouseCurrency wc = warehouseCurrencyRepository
                .findDefaultByCompanyIdAndWarehouseId(companyId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Default currency not set for warehouse " + warehouseId + " and company " + companyId));

        return mapToResponse(wc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseCurrencyResponse> listWarehouseCurrencies(Long companyId, Long warehouseId) {
        return warehouseCurrencyRepository.findByCompany_IdAndWarehouse_Id(companyId, warehouseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public WarehouseCurrencyResponse updateWarehouseCurrency(Long id, Long companyId, Long warehouseId,
            UpdateWarehouseCurrencyRequest request) {

        WarehouseCurrency wc = warehouseCurrencyRepository
                .findByIdAndCompanyIdAndWarehouseId(id, companyId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "WarehouseCurrency not found for id " + id + " company " + companyId + " warehouse "
                                + warehouseId));

        if (request.getCurrencyId() != null) {
            Currency currency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Currency not found with id: " + request.getCurrencyId()));
            wc.setCurrency(currency);
        }

        if (request.getDecimalPlaces() != null) {
            wc.setDecimalPlaces(request.getDecimalPlaces());
        }

        if (request.getDefaultCurrency() != null && request.getDefaultCurrency()) {
            boolean exists = warehouseCurrencyRepository.existsByCompanyIdAndWarehouseIdAndDefaultCurrencyTrueAndIdNot(
                    companyId, warehouseId, wc.getId());
            if (exists) {
                throw new IllegalStateException("Default currency already exists for warehouse " + warehouseId);
            }
            wc.setDefaultCurrency(true);
        } else {
            wc.setDefaultCurrency(false);
        }

        if (request.getStatus() != null) {
            wc.setStatus(request.getStatus());
        }

        WarehouseCurrency updated = warehouseCurrencyRepository.save(wc);
        return mapToResponse(updated);
    }

    @Override
    public void deleteWarehouseCurrency(Long id, Long companyId, Long warehouseId) {
        warehouseCurrencyRepository.deleteByIdAndCompanyIdAndWarehouseId(id, companyId, warehouseId);
        log.info("Deleted warehouse currency id={} for companyId={} warehouseId={}", id, companyId, warehouseId);
    }

    private WarehouseCurrencyResponse mapToResponse(WarehouseCurrency wc) {
        return WarehouseCurrencyResponse.builder()
                .id(wc.getId())
                .currencyId(wc.getCurrency().getId())
                .currencyCode(wc.getCurrency().getCode())
                .currencyName(wc.getCurrency().getName())
                .symbol(wc.getCurrency().getSymbol())
                .decimalPlaces(wc.getDecimalPlaces())
                .defaultCurrency(wc.isDefaultCurrency())
                .status(wc.getStatus())
                .companyId(wc.getCompany().getId())
                .warehouseId(wc.getWarehouse().getId())
                .build();
    }
}
