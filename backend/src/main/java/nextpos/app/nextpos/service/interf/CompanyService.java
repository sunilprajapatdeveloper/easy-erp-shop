package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyResponse;

public interface CompanyService {

    /**
     * Create a new company.
     */
    CompanyResponse createCompany(CreateCompanyRequest request);

    /**
     * Update company fields (partial updates supported).
     */
    CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request);

    /**
     * Get company by id (with settings embedded).
     */
    CompanyResponse getCompany(Long companyId);

    /**
     * Soft-delete a company (mark as deleted + de-activate).
     */
    void deleteCompany(Long companyId);
}