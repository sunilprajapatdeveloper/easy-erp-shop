package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanyRequest;
import nextpos.app.nextpos.model.dto.response.CompanyResponse;

public interface CompanyService {

    /**
     * Create a new company.
     *
     * @param request   create DTO
     * @param createdBy authenticated user id performing the creation
     * @return created company response
     */
    CompanyResponse createCompany(CreateCompanyRequest request, Long createdBy);

    /**
     * Update company fields (partial updates supported).
     *
     * @param companyId id of company to update
     * @param request   update DTO (optional fields)
     * @param updatedBy authenticated user id performing the update
     * @return updated company response
     */
    CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request, Long updatedBy);

    /**
     * Get company by id (with settings embedded).
     *
     * @param companyId company id
     * @return company response
     */
    CompanyResponse getCompany(Long companyId);

    /**
     * Soft-delete a company (mark as deleted + de-activate).
     *
     * @param companyId id to delete
     * @param deletedBy user id performing deletion
     */
    void deleteCompany(Long companyId, Long deletedBy);
}