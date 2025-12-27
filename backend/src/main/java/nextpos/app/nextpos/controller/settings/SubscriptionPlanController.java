package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.response.SubscriptionPlanResponse;
import nextpos.app.nextpos.service.interf.SubscriptionPlanService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    /**
     * Create a new subscription plan.
     * Header:
     * X-User-Id : id of user performing the action (used as createdBy)
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponse> createSubscriptionPlan(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        log.info("CreateSubscriptionPlan request by userId={}", userId);
        SubscriptionPlanResponse response = subscriptionPlanService.createSubscriptionPlan(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get subscription plan by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> getSubscriptionPlan(@PathVariable("id") Long id) {
        SubscriptionPlanResponse response = subscriptionPlanService.getSubscriptionPlan(id);
        return ResponseEntity.ok(response);
    }

    /**
     * List all subscription plans (non-deleted).
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponse>> listSubscriptionPlans() {
        List<SubscriptionPlanResponse> list = subscriptionPlanService.listSubscriptionPlans();
        return ResponseEntity.ok(list);
    }

    /**
     * Update an existing subscription plan.
     * Header:
     * X-User-Id : id of user performing the action (used as updatedBy)
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> updateSubscriptionPlan(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdateSubscriptionPlanRequest request) {

        log.info("UpdateSubscriptionPlan id={} by userId={}", id, userId);
        // Set updatedBy in request DTO to ensure service has the correct audit info
        request.setUpdatedBy(userId);

        SubscriptionPlanResponse response = subscriptionPlanService.updateSubscriptionPlan(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft-delete a subscription plan (mark as deleted).
     * Header:
     * X-User-Id : id of user performing the action (used as deletedBy)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {

        log.info("Delete(soft) SubscriptionPlan id={} by userId={}", id, userId);
        subscriptionPlanService.deleteSubscriptionPlan(id, userId);
        return ResponseEntity.noContent().build();
    }
}
