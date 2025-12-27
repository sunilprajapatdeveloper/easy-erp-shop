package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.response.CompanySubscriptionResponse;
import nextpos.app.nextpos.model.entity.CompanySubscription;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Company Subscriptions.
 * Defines CRUD operations and retrieval methods for company subscriptions.
 */
public interface CompanySubscriptionService {

    /**
     * Create a new company subscription.
     *
     * @param request   DTO containing subscription creation details
     * @param createdBy User ID who is creating the subscription
     * @return CompanySubscriptionResponse DTO with created subscription details
     */
    CompanySubscriptionResponse createCompanySubscription(CreateCompanySubscriptionRequest request, Long createdBy);

    /**
     * Update an existing company subscription.
     *
     * @param subscriptionId ID of the subscription to update
     * @param request        DTO containing fields to update
     * @param updatedBy      User ID who is performing the update
     * @return CompanySubscriptionResponse DTO with updated subscription details
     */
    CompanySubscriptionResponse updateCompanySubscription(Long subscriptionId,
            UpdateCompanySubscriptionRequest request,
            Long updatedBy);

    /**
     * Soft delete a company subscription.
     *
     * @param subscriptionId ID of the subscription to delete
     * @param deletedBy      User ID performing the deletion
     */
    void deleteCompanySubscription(Long subscriptionId, Long deletedBy);

    /**
     * Get the active subscription of a company, if any.
     *
     * @param companyId ID of the company
     * @return Optional containing the active CompanySubscription if present
     */
    Optional<CompanySubscription> getActiveSubscription(Long companyId);

    /**
     * List all subscriptions (active + inactive) of a company.
     *
     * @param companyId ID of the company
     * @return List of CompanySubscription entities
     */
    List<CompanySubscription> listSubscriptionsByCompany(Long companyId);
}