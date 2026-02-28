package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.response.CompanySubscriptionResponse;
import nextpos.app.nextpos.service.interf.CompanySubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for CompanySubscription operations.
 *
 * Endpoints:
 * - POST /api/v1/company-subscriptions (create)
 * - PUT /api/v1/company-subscriptions/{id} (update)
 * - DELETE /api/v1/company-subscriptions/{id} (soft delete)
 * - GET /api/v1/company-subscriptions/company/{companyId} (list all for
 * company)
 * - GET /api/v1/company-subscriptions/company/{companyId}/active (get active
 * subscription)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/company-subscriptions")
@RequiredArgsConstructor
public class CompanySubscriptionController {

    private final CompanySubscriptionService companySubscriptionService;

    @PostMapping
    public ResponseEntity<CompanySubscriptionResponse> createCompanySubscription(
            @Valid @RequestBody CreateCompanySubscriptionRequest request) {

        log.info("CreateCompanySubscription request for companyId={}", request.getCompanyId());
        CompanySubscriptionResponse response = companySubscriptionService.createCompanySubscription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanySubscriptionResponse> updateCompanySubscription(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCompanySubscriptionRequest request) {

        log.info("UpdateCompanySubscription id={}", id);
        CompanySubscriptionResponse response = companySubscriptionService.updateCompanySubscription(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanySubscription(
            @PathVariable("id") Long id) {

        log.info("Delete (soft) CompanySubscription id={}", id);
        companySubscriptionService.deleteCompanySubscription(id);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping("/company/{companyId}")
    // public ResponseEntity<List<CompanySubscriptionResponse>>
    // listSubscriptionsByCompany(
    // @PathVariable("companyId") Long companyId) {
    // log.info("List subscriptions for companyId={}", companyId);
    // List<CompanySubscriptionResponse> subscriptions = companySubscriptionService
    // .listSubscriptionsByCompanyResponse(companyId);
    // return ResponseEntity.ok(subscriptions);
    // }

    // @GetMapping("/company/{companyId}/active")
    // public ResponseEntity<CompanySubscriptionResponse> getActiveSubscription(
    // @PathVariable("companyId") Long companyId) {
    // log.info("Get active subscription for companyId={}", companyId);
    // return companySubscriptionService.getActiveSubscriptionResponse(companyId)
    // .map(ResponseEntity::ok)
    // .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    // }
}
