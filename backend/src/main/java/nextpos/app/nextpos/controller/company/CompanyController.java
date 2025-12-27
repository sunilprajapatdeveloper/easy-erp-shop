package nextpos.app.nextpos.controller.company;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyResponse;
import nextpos.app.nextpos.service.interf.CompanyService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies")
@Validated
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Create a new Company along with its default settings.
     * 
     * @param request   CreateCompanyRequest payload
     * @param createdBy User ID performing the operation (from security context or
     *                  header)
     */
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody @Validated CreateCompanyRequest request,
            @RequestHeader("X-User-Id") Long createdBy) {
        CompanyResponse response = companyService.createCompany(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing Company by ID.
     * 
     * @param companyId ID of the company to update
     * @param request   UpdateCompanyRequest payload
     * @param updatedBy User ID performing the operation (from security context or
     *                  header)
     */
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @RequestBody @Validated UpdateCompanyRequest request,
            @RequestHeader("X-User-Id") Long updatedBy) {
        CompanyResponse response = companyService.updateCompany(companyId, request, updatedBy);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a company by ID
     * 
     * @param companyId ID of the company
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompany(companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a company by ID
     * 
     * @param companyId ID of the company
     * @param updatedBy User ID performing the deletion
     */
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> softDeleteCompany(
            @PathVariable Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy) {
        companyService.deleteCompany(companyId, updatedBy);
        return ResponseEntity.noContent().build();
    }
}
