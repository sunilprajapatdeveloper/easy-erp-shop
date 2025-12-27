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
     * Company ID is passed in header: X-Company-Id
     */
    @PostMapping
    public ResponseEntity<CompanyCurrencyResponse> createCompanyCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @Valid @RequestBody CreateCompanyCurrencyRequest request) {

        CompanyCurrencyResponse response = companyCurrencyService.createCompanyCurrency(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a single Company Currency by ID
     * Company ID is passed in header: X-Company-Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> getCompanyCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long id) {

        CompanyCurrencyResponse response = companyCurrencyService.getCompanyCurrency(id, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all Company Currencies for a company
     * Company ID is passed in header: X-Company-Id
     */
    @GetMapping
    public ResponseEntity<List<CompanyCurrencyResponse>> listCompanyCurrencies(
            @RequestHeader("X-Company-Id") Long companyId) {

        List<CompanyCurrencyResponse> response = companyCurrencyService.listCompanyCurrencies(companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a Company Currency
     * Company ID is passed in header: X-Company-Id
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> updateCompanyCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyCurrencyRequest request) {

        CompanyCurrencyResponse response = companyCurrencyService.updateCompanyCurrency(id, companyId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a Company Currency
     * Company ID is passed in header: X-Company-Id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long id) {

        companyCurrencyService.deleteCompanyCurrency(id, companyId);
        return ResponseEntity.noContent().build();
    }
}
