package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.response.CurrencyResponse;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.service.interf.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyResponse getCurrencyById(Long id) {
        return currencyRepository.findById(id)
                .map(CurrencyResponse::new)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    @Override
    public List<CurrencyResponse> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(CurrencyResponse::new)
                .collect(Collectors.toList());
    }
}
