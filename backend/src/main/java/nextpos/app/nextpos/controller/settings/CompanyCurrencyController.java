package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyCurrencyResponse;
import nextpos.app.nextpos.service.interf.CompanyCurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company-currencies")
@RequiredArgsConstructor
public class CompanyCurrencyController {

    private final CompanyCurrencyService companyCurrencyService;

    /**
     * Create a new Company Currency
     */
    @PostMapping
    public ResponseEntity<CompanyCurrencyResponse> createCompanyCurrency(
            @Valid @RequestBody CreateCompanyCurrencyRequest request) {

        CompanyCurrencyResponse response = companyCurrencyService.createCompanyCurrency(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a single Company Currency by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> getCompanyCurrency(
            @PathVariable Long id) {

        CompanyCurrencyResponse response = companyCurrencyService.getCompanyCurrency(id);
        return ResponseEntity.ok(response);
    }

    /**
     * List all Company Currencies for a company
     */
    @GetMapping
    public ResponseEntity<List<CompanyCurrencyResponse>> listCompanyCurrencies() {

        List<CompanyCurrencyResponse> response = companyCurrencyService.listCompanyCurrencies();
        return ResponseEntity.ok(response);
    }

    /**
     * Update a Company Currency
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> updateCompanyCurrency(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyCurrencyRequest request) {

        CompanyCurrencyResponse response = companyCurrencyService.updateCompanyCurrency(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a Company Currency
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyCurrency(
            @PathVariable Long id) {

        companyCurrencyService.deleteCompanyCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
