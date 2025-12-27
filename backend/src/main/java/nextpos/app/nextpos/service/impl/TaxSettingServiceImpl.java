package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateTaxSettingRequest;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.TaxSetting;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.TaxSettingRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.service.interf.TaxSettingService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaxSettingServiceImpl implements TaxSettingService {

    private final TaxSettingRepository taxSettingRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public TaxSettingResponse createTaxSetting(CreateTaxSettingRequest request, Long companyId) {
        log.info("Creating TaxSetting for companyId={}", companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + companyId));

        Warehouse warehouse = null;
        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + request.getWarehouseId()));
        }

        TaxSetting taxSetting = TaxSetting.builder()
                .company(company)
                .warehouse(warehouse)
                .taxType(request.getTaxType())
                .name(request.getName())
                .rate(request.getRate())
                .calculationType(request.getCalculationType())
                .inclusiveType(request.getInclusiveType())
                .active(request.isActive())
                .regionCode(request.getRegionCode())
                .description(request.getDescription())
                .build();

        TaxSetting saved = taxSettingRepository.save(taxSetting);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaxSettingResponse getTaxSetting(Long id, Long companyId) {
        TaxSetting taxSetting = taxSettingRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "TaxSetting not found for id=" + id + " companyId=" + companyId));

        return mapToResponse(taxSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxSettingResponse> listTaxSettings(Long companyId) {
        return taxSettingRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaxSettingResponse updateTaxSetting(Long id, Long companyId, UpdateTaxSettingRequest request) {
        TaxSetting taxSetting = taxSettingRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "TaxSetting not found for id=" + id + " companyId=" + companyId));

        if (request.getTaxType() != null)
            taxSetting.setTaxType(request.getTaxType());
        if (request.getName() != null)
            taxSetting.setName(request.getName());
        if (request.getRate() != null)
            taxSetting.setRate(request.getRate());
        if (request.getCalculationType() != null)
            taxSetting.setCalculationType(request.getCalculationType());
        if (request.getInclusiveType() != null)
            taxSetting.setInclusiveType(request.getInclusiveType());
        if (request.getActive() != null)
            taxSetting.setActive(request.getActive());
        if (request.getRegionCode() != null)
            taxSetting.setRegionCode(request.getRegionCode());
        if (request.getDescription() != null)
            taxSetting.setDescription(request.getDescription());

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + request.getWarehouseId()));
            taxSetting.setWarehouse(warehouse);
        }

        TaxSetting updated = taxSettingRepository.save(taxSetting);
        return mapToResponse(updated);
    }

    @Override
    public void deleteTaxSetting(Long id, Long companyId) {
        log.info("Deleting TaxSetting id={} for companyId={}", id, companyId);
        if (!taxSettingRepository.findByIdAndCompanyId(id, companyId).isPresent()) {
            throw new EntityNotFoundException("TaxSetting not found for id=" + id + " companyId=" + companyId);
        }
        taxSettingRepository.deleteByIdAndCompanyId(id, companyId);
    }

    private TaxSettingResponse mapToResponse(TaxSetting taxSetting) {
        return TaxSettingResponse.builder()
                .taxType(taxSetting.getTaxType())
                .name(taxSetting.getName())
                .rate(taxSetting.getRate())
                .calculationType(taxSetting.getCalculationType())
                .inclusiveType(taxSetting.getInclusiveType())
                .active(taxSetting.isActive())
                .regionCode(taxSetting.getRegionCode())
                .description(taxSetting.getDescription())
                .warehouseId(taxSetting.getWarehouse() != null ? taxSetting.getWarehouse().getId() : null)
                .build();
    }
}