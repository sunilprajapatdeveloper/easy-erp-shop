package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCompanySubscriptionRequest;
import nextpos.app.nextpos.model.dto.response.CompanySubscriptionResponse;
import nextpos.app.nextpos.model.dto.response.SubscriptionPlanResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.CompanySubscription;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.model.enums.SubscriptionStatus;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.CompanySubscriptionRepository;
import nextpos.app.nextpos.repository.SubscriptionPlanRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.CompanySubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing company subscriptions.
 * - Validates input
 * - Ensures single active subscription per company
 * - Applies reasonable defaults for dates (trial/end/nextBilling) when missing
 * - Maps entities to response DTOs
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanySubscriptionServiceImpl implements CompanySubscriptionService {

    private final CompanySubscriptionRepository companySubscriptionRepository;
    private final CompanyRepository companyRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public CompanySubscriptionResponse createCompanySubscription(CreateCompanySubscriptionRequest request) {
        Long authenticatedCompanyId = UserContext.getCurrentCompanyId();
        if (!authenticatedCompanyId.equals(request.getCompanyId())) {
            throw new SecurityException("Company identifier does not match authenticated tenant");
        }
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        // Validate that the user belongs to the same company as the subscription
        // request
        if (!currentCompanyId.equals(request.getCompanyId())) {
            throw new SecurityException("You can only create subscriptions for your own company");
        }

        // Validate company
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + request.getCompanyId()));

        // Validate plan
        SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException(
                        "SubscriptionPlan not found with id: " + request.getSubscriptionPlanId()));

        // Business rule: prevent multiple active subscriptions for the same company
        companySubscriptionRepository.findActiveSubscriptionByCompanyId(company.getId())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            String.format("Company with id %d already has an active subscription (subscriptionId=%d).",
                                    company.getId(), existing.getId()));
                });

        // Use provided startDate (Create DTO requires it). If null (unexpected),
        // fallback to now.
        LocalDateTime startDate = request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now();

        // Trial handling: if client asked to activate trial, the plan must support
        // trial
        boolean trialActive = Boolean.TRUE.equals(request.getTrialActive());
        LocalDateTime trialEndDate = request.getTrialEndDate();
        if (trialActive) {
            if (!plan.isTrialAvailable()) {
                throw new IllegalArgumentException(
                        "Plan does not support trial, cannot activate trial for this subscription.");
            }
            if (trialEndDate == null) {
                int planTrialDays = plan.getTrialDays();
                if (planTrialDays > 0) {
                    trialEndDate = startDate.plusDays(planTrialDays);
                } else {
                    // If plan has no trial days but trialActive requested, treat as invalid
                    throw new IllegalArgumentException("Trial was requested but plan does not define trial days.");
                }
            }
        } else {
            // Ensure we don't accidentally set trialEndDate when trial not active
            trialEndDate = null;
        }

        // End date: use provided endDate or infer from billing cycle (one billing
        // period after start).
        LocalDateTime endDate = request.getEndDate();
        if (endDate == null) {
            // If trial is active, set endDate to trialEndDate (trial period). Otherwise,
            // derive by billing cycle.
            if (trialActive && trialEndDate != null) {
                endDate = trialEndDate;
            } else {
                endDate = addBillingCycle(startDate, plan.getBillingCycle());
            }
        }

        // Next billing date: if provided use it; otherwise:
        // - if trialActive then set nextBillingDate = trialEndDate
        // - else set nextBillingDate = startDate + billingCycle
        LocalDateTime nextBillingDate = request.getNextBillingDate();
        if (nextBillingDate == null) {
            if (trialActive && trialEndDate != null) {
                nextBillingDate = trialEndDate;
            } else {
                nextBillingDate = addBillingCycle(startDate, plan.getBillingCycle());
            }
        }

        // Renewal date: if provided use it, otherwise set to endDate (best-effort)
        LocalDateTime renewalDate = request.getRenewalDate() != null ? request.getRenewalDate() : endDate;

        // Build subscription
        CompanySubscription subscription = CompanySubscription.builder()
                .company(company)
                .subscriptionPlan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .nextBillingDate(nextBillingDate)
                .renewalDate(renewalDate)
                .autoRenew(request.getAutoRenew() != null ? request.getAutoRenew() : true)
                .trialActive(trialActive)
                .trialEndDate(trialEndDate)
                .status(SubscriptionStatus.ACTIVE) // new subscriptions are ACTIVE by default (business rule)
                .billingReference(request.getBillingReference())
                .createdBy(currentUserId)
                .updatedBy(currentUserId)
                .build();

        CompanySubscription saved = companySubscriptionRepository.save(subscription);

        log.info("Created CompanySubscription id={} for companyId={}", saved.getId(), company.getId());

        return mapToSubscriptionResponse(saved);
    }

    @Override
    public CompanySubscriptionResponse updateCompanySubscription(Long subscriptionId,
            UpdateCompanySubscriptionRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        CompanySubscription subscription = companySubscriptionRepository
                .findByIdAndCompanyIdAndIsDeletedFalse(subscriptionId, currentCompanyId)
                .orElseThrow(
                        () -> new EntityNotFoundException("CompanySubscription not found with id: " + subscriptionId));

        // Ensure the subscription belongs to the user's company
        if (!currentCompanyId.equals(subscription.getCompany().getId())) {
            throw new SecurityException("You can only update subscriptions for your own company");
        }

        // If plan updated, validate plan
        if (request.getSubscriptionPlanId() != null) {
            SubscriptionPlan newPlan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
                    .filter(p -> !p.isDeleted())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "SubscriptionPlan not found with id: " + request.getSubscriptionPlanId()));
            subscription.setSubscriptionPlan(newPlan);
        }

        if (request.getStartDate() != null) {
            subscription.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            subscription.setEndDate(request.getEndDate());
        }

        if (request.getNextBillingDate() != null) {
            subscription.setNextBillingDate(request.getNextBillingDate());
        }

        if (request.getRenewalDate() != null) {
            subscription.setRenewalDate(request.getRenewalDate());
        }

        if (request.getAutoRenew() != null) {
            subscription.setAutoRenew(request.getAutoRenew());
        }

        // Trial toggles: if enabling trial, ensure plan supports it and compute
        // trialEndDate if missing.
        if (request.getTrialActive() != null) {
            boolean requestedTrial = request.getTrialActive();
            SubscriptionPlan plan = subscription.getSubscriptionPlan();
            if (requestedTrial && !plan.isTrialAvailable()) {
                throw new IllegalArgumentException(
                        "Plan does not support trial; cannot enable trial for this subscription.");
            }
            subscription.setTrialActive(requestedTrial);

            if (requestedTrial) {
                LocalDateTime trialEnd = request.getTrialEndDate();
                if (trialEnd == null) {
                    int planTrialDays = plan.getTrialDays();
                    if (planTrialDays > 0) {
                        LocalDateTime base = request.getStartDate() != null ? request.getStartDate()
                                : subscription.getStartDate();
                        trialEnd = base.plusDays(planTrialDays);
                    } else {
                        throw new IllegalArgumentException("Trial was requested but plan does not define trial days.");
                    }
                }
                subscription.setTrialEndDate(trialEnd);
            } else {
                // disabling trial: clear trialEndDate
                subscription.setTrialEndDate(null);
            }
        } else if (request.getTrialEndDate() != null) {
            // only update trialEndDate if explicitly provided
            subscription.setTrialEndDate(request.getTrialEndDate());
        }

        if (request.getBillingReference() != null) {
            subscription.setBillingReference(request.getBillingReference());
        }

        if (request.getStatus() != null) {
            subscription.setStatus(request.getStatus());
        }

        subscription.setUpdatedBy(currentUserId);
        subscription.setUpdatedAt(LocalDateTime.now());

        CompanySubscription updated = companySubscriptionRepository.save(subscription);

        log.info("Updated CompanySubscription id={} for companyId={}", updated.getId(),
                updated.getCompany() != null ? updated.getCompany().getId() : null);

        return mapToSubscriptionResponse(updated);
    }

    @Override
    public void deleteCompanySubscription(Long subscriptionId) {
        Long currentUserId = UserContext.getCurrentUserId();
        Long currentCompanyId = UserContext.getCurrentCompanyId();

        CompanySubscription subscription = companySubscriptionRepository
                .findByIdAndCompanyIdAndIsDeletedFalse(subscriptionId, currentCompanyId)
                .orElseThrow(
                        () -> new EntityNotFoundException("CompanySubscription not found with id: " + subscriptionId));

        // Ensure the subscription belongs to the user's company
        if (!currentCompanyId.equals(subscription.getCompany().getId())) {
            throw new SecurityException("You can only delete subscriptions for your own company");
        }

        subscription.setDeleted(true);
        subscription.setUpdatedBy(currentUserId);
        subscription.setUpdatedAt(LocalDateTime.now());

        companySubscriptionRepository.save(subscription);

        log.info("Soft deleted CompanySubscription id={} for companyId={}", subscription.getId(),
                subscription.getCompany() != null ? subscription.getCompany().getId() : null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanySubscription> getActiveSubscription(Long companyId) {
        // No need to validate company access here because it's a read operation;
        // we assume the caller (controller) will handle security if needed.
        return companySubscriptionRepository.findActiveSubscriptionByCompanyId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanySubscription> listSubscriptionsByCompany(Long companyId) {
        return companySubscriptionRepository.findByCompanyIdAndIsDeletedFalse(companyId);
    }

    /* ----------------------- Mapping helpers ----------------------- */

    private CompanySubscriptionResponse mapToSubscriptionResponse(CompanySubscription subscription) {
        SubscriptionPlan plan = subscription.getSubscriptionPlan();

        SubscriptionPlanResponse planResp = plan != null ? mapToSubscriptionPlanResponse(plan) : null;

        return CompanySubscriptionResponse.builder()
                .id(subscription.getId())
                .companyId(subscription.getCompany() != null ? subscription.getCompany().getId() : null)
                .subscriptionPlanId(plan != null ? plan.getId() : null)
                .plan(planResp)
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .nextBillingDate(subscription.getNextBillingDate())
                .renewalDate(subscription.getRenewalDate())
                .trialActive(subscription.isTrialActive())
                .trialEndDate(subscription.getTrialEndDate())
                .autoRenew(subscription.isAutoRenew())
                .billingReference(subscription.getBillingReference())
                .createdBy(subscription.getCreatedBy())
                .createdAt(subscription.getCreatedAt())
                .updatedBy(subscription.getUpdatedBy())
                .updatedAt(subscription.getUpdatedAt())
                .isDeleted(subscription.isDeleted())
                .version(subscription.getVersion())
                .build();
    }

    private SubscriptionPlanResponse mapToSubscriptionPlanResponse(SubscriptionPlan plan) {
        return SubscriptionPlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .currency(plan.getCurrency())
                .billingCycle(plan.getBillingCycle())
                .trialAvailable(plan.isTrialAvailable())
                .trialDays(plan.getTrialDays())
                .maxUsers(plan.getMaxUsers())
                .maxBranches(plan.getMaxBranches())
                .features(plan.getFeatures())
                .availableRegions(plan.getAvailableRegions())
                .status(plan.getStatus())
                .isDeleted(plan.isDeleted())
                .createdBy(plan.getCreatedBy())
                .updatedBy(plan.getUpdatedBy())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .version(plan.getVersion())
                .build();
    }

    /**
     * Adds one billing period to the given date depending on billing cycle.
     * Reasonable default used to infer end/next billing date when client didn't
     * provide them.
     */
    private LocalDateTime addBillingCycle(LocalDateTime base, BillingCycle billingCycle) {
        if (base == null || billingCycle == null) {
            return base;
        }

        switch (billingCycle) {
            case DAILY:
                return base.plusDays(1);
            case WEEKLY:
                return base.plusWeeks(1);
            case MONTHLY:
                return base.plusMonths(1);
            case YEARLY:
                return base.plusYears(1);
            default:
                // fallback: return base + 1 month
                return base.plusMonths(1);
        }
    }
}
