package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.response.SubscriptionPlanResponse;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.enums.PlanStatus;
import nextpos.app.nextpos.repository.SubscriptionPlanRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.SubscriptionPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();

        // Ensure unique plan name + billingCycle
        if (subscriptionPlanRepository.existsByNameAndBillingCycleAndIsDeletedFalse(
                request.getName(), request.getBillingCycle())) {
            throw new IllegalArgumentException(
                    "Subscription plan with this name and billing cycle already exists.");
        }

        SubscriptionPlan plan = SubscriptionPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .billingCycle(request.getBillingCycle())
                .trialAvailable(request.isTrialAvailable())
                .trialDays(request.getTrialDays())
                .maxUsers(request.getMaxUsers())
                .maxBranches(request.getMaxBranches())
                .features(request.getFeatures())
                .availableRegions(request.getAvailableRegions())
                .status(PlanStatus.ACTIVE)
                .isDeleted(false)
                .createdBy(currentUserId)
                .updatedBy(currentUserId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SubscriptionPlan saved = subscriptionPlanRepository.save(plan);
        log.info("Created SubscriptionPlan id={} name={}", saved.getId(), saved.getName());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getSubscriptionPlan(Long id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id " + id));

        return toResponse(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> listSubscriptionPlans() {
        return subscriptionPlanRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SubscriptionPlanResponse updateSubscriptionPlan(Long id, UpdateSubscriptionPlanRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();

        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id " + id));

        if (request.getName() != null)
            plan.setName(request.getName());
        if (request.getDescription() != null)
            plan.setDescription(request.getDescription());
        if (request.getPrice() != null)
            plan.setPrice(request.getPrice());
        if (request.getCurrency() != null)
            plan.setCurrency(request.getCurrency());
        if (request.getBillingCycle() != null)
            plan.setBillingCycle(request.getBillingCycle());
        if (request.getTrialAvailable() != null)
            plan.setTrialAvailable(request.getTrialAvailable());
        if (request.getTrialDays() != null)
            plan.setTrialDays(request.getTrialDays());
        if (request.getMaxUsers() != null)
            plan.setMaxUsers(request.getMaxUsers());
        if (request.getMaxBranches() != null)
            plan.setMaxBranches(request.getMaxBranches());
        if (request.getFeatures() != null)
            plan.setFeatures(request.getFeatures());
        if (request.getAvailableRegions() != null)
            plan.setAvailableRegions(request.getAvailableRegions());
        if (request.getStatus() != null)
            plan.setStatus(request.getStatus());

        plan.setUpdatedBy(currentUserId);
        plan.setUpdatedAt(LocalDateTime.now());

        SubscriptionPlan updated = subscriptionPlanRepository.save(plan);
        log.info("Updated SubscriptionPlan id={} name={}", updated.getId(), updated.getName());

        return toResponse(updated);
    }

    @Override
    public void deleteSubscriptionPlan(Long id) {
        Long currentUserId = UserContext.getCurrentUserId();

        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id " + id));

        plan.setDeleted(true);
        plan.setUpdatedBy(currentUserId);
        plan.setUpdatedAt(LocalDateTime.now());

        subscriptionPlanRepository.save(plan);
        log.info("Soft deleted SubscriptionPlan id={}", id);
    }

    /**
     * Convert entity to response DTO.
     */
    private SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
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
}
