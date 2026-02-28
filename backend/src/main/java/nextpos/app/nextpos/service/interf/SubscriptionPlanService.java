package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSubscriptionPlanRequest;
import nextpos.app.nextpos.model.dto.response.SubscriptionPlanResponse;

import java.util.List;

public interface SubscriptionPlanService {

    SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request);

    SubscriptionPlanResponse getSubscriptionPlan(Long id);

    List<SubscriptionPlanResponse> listSubscriptionPlans();

    SubscriptionPlanResponse updateSubscriptionPlan(Long id, UpdateSubscriptionPlanRequest request);

    void deleteSubscriptionPlan(Long id);
}