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
     */
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody @Validated CreateCompanyRequest request) {
        CompanyResponse response = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing Company by ID.
     */
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @RequestBody @Validated UpdateCompanyRequest request) {
        CompanyResponse response = companyService.updateCompany(companyId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a company by ID
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompany(companyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a company by ID
     */
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> softDeleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
