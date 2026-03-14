package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyCurrencyResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.CompanyCurrency;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.CompanyCurrencyRepository;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.service.interf.CompanyCurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyCurrencyServiceImpl implements CompanyCurrencyService {

    private final CompanyCurrencyRepository companyCurrencyRepository;
    private final CurrencyRepository currencyRepository;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyCurrencyResponse createCompanyCurrency(Long companyId, CreateCompanyCurrencyRequest request) {
        log.info("Creating company currency for companyId={} currencyId={}", companyId, request.getCurrencyId());

        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        // Only one default currency per company
        if (request.isDefaultCurrency()
                && companyCurrencyRepository.existsByCompanyIdAndDefaultCurrencyTrue(companyId)) {
            throw new IllegalStateException("Default currency already exists for company " + companyId);
        }

        CompanyCurrency companyCurrency = CompanyCurrency.builder()
                .currency(currency)
                .company(company)
                .decimalPlaces(request.getDecimalPlaces() != null ? request.getDecimalPlaces() : 2)
                .defaultCurrency(request.isDefaultCurrency())
                .status(request.getStatus())
                .build();

        CompanyCurrency saved = companyCurrencyRepository.save(companyCurrency);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyCurrencyResponse getCompanyCurrency(Long id, Long companyId) {
        CompanyCurrency currency = companyCurrencyRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CompanyCurrency not found for id " + id + " and company " + companyId));
        return mapToResponse(currency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyCurrencyResponse> listCompanyCurrencies(Long companyId) {
        return companyCurrencyRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CompanyCurrencyResponse updateCompanyCurrency(Long id, Long companyId,
            UpdateCompanyCurrencyRequest request) {
        CompanyCurrency currency = companyCurrencyRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CompanyCurrency not found for id " + id + " and company " + companyId));

        if (request.getCurrencyId() != null) {
            Currency newCurrency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Currency not found with id: " + request.getCurrencyId()));
            currency.setCurrency(newCurrency);
        }

        if (request.getDecimalPlaces() != null) {
            currency.setDecimalPlaces(request.getDecimalPlaces());
        }

        if (request.getDefaultCurrency() != null) {
            if (request.getDefaultCurrency() && companyCurrencyRepository
                    .existsByCompanyIdAndDefaultCurrencyTrueAndIdNot(companyId, currency.getId())) {
                throw new IllegalStateException("Default currency already exists for company " + companyId);
            }
            currency.setDefaultCurrency(request.getDefaultCurrency());
        }

        if (request.getStatus() != null) {
            currency.setStatus(request.getStatus());
        }

        CompanyCurrency updated = companyCurrencyRepository.save(currency);
        return mapToResponse(updated);
    }

    @Override
    public void deleteCompanyCurrency(Long id, Long companyId) {
        int deleted = companyCurrencyRepository.deleteByIdAndCompanyId(id, companyId);
        if (deleted == 0) {
            throw new EntityNotFoundException("CompanyCurrency not found for id " + id + " and company " + companyId);
        }
        log.info("Deleted company currency id={} for companyId={}", id, companyId);
    }

    private CompanyCurrencyResponse mapToResponse(CompanyCurrency entity) {
        return CompanyCurrencyResponse.builder()
                .id(entity.getId())
                .currencyId(entity.getCurrency().getId())
                .currencyCode(entity.getCurrency().getCode())
                .currencyName(entity.getCurrency().getName())
                .symbol(entity.getCurrency().getSymbol())
                .decimalPlaces(entity.getDecimalPlaces())
                .defaultCurrency(entity.isDefaultCurrency())
                .status(entity.getStatus())
                .companyId(entity.getCompany().getId())
                .build();
    }
}
