package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExchangeRateRequest;
import nextpos.app.nextpos.model.dto.response.ExchangeRateResponse;
import nextpos.app.nextpos.service.interf.ExchangeRateService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    /**
     * Create a new exchange rate
     */
    @PostMapping
    public ResponseEntity<ExchangeRateResponse> createExchangeRate(
            @Valid @RequestBody CreateExchangeRateRequest request) {
        ExchangeRateResponse response = exchangeRateService.createExchangeRate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing exchange rate by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExchangeRateResponse> updateExchangeRate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExchangeRateRequest request) {
        ExchangeRateResponse response = exchangeRateService.updateExchangeRate(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get an exchange rate by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(@PathVariable Long id) {
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an exchange rate by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable Long id) {
        exchangeRateService.deleteExchangeRate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all exchange rates
     */
    @GetMapping
    public ResponseEntity<List<ExchangeRateResponse>> getAllExchangeRates() {
        List<ExchangeRateResponse> responseList = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok(responseList);
    }

    /**
     * Find exchange rate by base/target currency and optional company/warehouse
     * scope
     */
    @GetMapping("/find")
    public ResponseEntity<ExchangeRateResponse> findExchangeRate(
            @RequestParam Long baseCurrencyId,
            @RequestParam Long targetCurrencyId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long warehouseId) {
        ExchangeRateResponse response = exchangeRateService.findExchangeRate(
                baseCurrencyId, targetCurrencyId, companyId, warehouseId);
        return ResponseEntity.ok(response);
    }
}
