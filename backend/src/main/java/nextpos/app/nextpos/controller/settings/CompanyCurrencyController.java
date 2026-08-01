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
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.security.onboarding.OnboardingTokenService;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/company-currencies")
@RequiredArgsConstructor
public class CompanyCurrencyController {

    private final CompanyCurrencyService companyCurrencyService;
    private final OnboardingTokenService onboardingTokenService;

    private Long resolveCompanyId(String onboardingToken) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            return UserContext.getCurrentCompanyId();
        }
        return onboardingTokenService.verify(onboardingToken).companyId();
    }

    /**
     * Create a new Company Currency
     * Company ID is passed in header: X-Company-Id
     */
    @PostMapping
    public ResponseEntity<CompanyCurrencyResponse> createCompanyCurrency(
            @RequestHeader(value = "X-Onboarding-Token", required = false) String onboardingToken,
            @Valid @RequestBody CreateCompanyCurrencyRequest request) {
        Long companyId = resolveCompanyId(onboardingToken);
        CompanyCurrencyResponse response = companyCurrencyService.createCompanyCurrency(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a single Company Currency by ID
     * Company ID is passed in header: X-Company-Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> getCompanyCurrency(
            @RequestHeader(value = "X-Onboarding-Token", required = false) String onboardingToken,
            @PathVariable Long id) {
        Long companyId = resolveCompanyId(onboardingToken);
        CompanyCurrencyResponse response = companyCurrencyService.getCompanyCurrency(id, companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all Company Currencies for a company
     * Company ID is passed in header: X-Company-Id
     */
    @GetMapping
    public ResponseEntity<List<CompanyCurrencyResponse>> listCompanyCurrencies(
            @RequestHeader(value = "X-Onboarding-Token", required = false) String onboardingToken) {
        Long companyId = resolveCompanyId(onboardingToken);
        List<CompanyCurrencyResponse> response = companyCurrencyService.listCompanyCurrencies(companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a Company Currency
     * Company ID is passed in header: X-Company-Id
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyCurrencyResponse> updateCompanyCurrency(
            @RequestHeader(value = "X-Onboarding-Token", required = false) String onboardingToken,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyCurrencyRequest request) {
        Long companyId = resolveCompanyId(onboardingToken);
        CompanyCurrencyResponse response = companyCurrencyService.updateCompanyCurrency(id, companyId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a Company Currency
     * Company ID is passed in header: X-Company-Id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyCurrency(
            @RequestHeader(value = "X-Onboarding-Token", required = false) String onboardingToken,
            @PathVariable Long id) {
        Long companyId = resolveCompanyId(onboardingToken);
        companyCurrencyService.deleteCompanyCurrency(id, companyId);
        return ResponseEntity.noContent().build();
    }
}
