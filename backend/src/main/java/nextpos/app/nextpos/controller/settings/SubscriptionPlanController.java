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
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponse> createSubscriptionPlan(
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        log.info("CreateSubscriptionPlan request");
        SubscriptionPlanResponse response = subscriptionPlanService.createSubscriptionPlan(request);
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
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> updateSubscriptionPlan(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateSubscriptionPlanRequest request) {

        log.info("UpdateSubscriptionPlan id={}", id);
        SubscriptionPlanResponse response = subscriptionPlanService.updateSubscriptionPlan(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft-delete a subscription plan (mark as deleted).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(
            @PathVariable("id") Long id) {

        log.info("Delete(soft) SubscriptionPlan id={}", id);
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.noContent().build();
    }
}
