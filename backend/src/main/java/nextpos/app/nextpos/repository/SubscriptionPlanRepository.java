package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import nextpos.app.nextpos.model.enums.BillingCycle;
import nextpos.app.nextpos.model.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    List<SubscriptionPlan> findAllByIsDeletedFalse();

    List<SubscriptionPlan> findAllByStatusAndIsDeletedFalse(PlanStatus status);

    Optional<SubscriptionPlan> findByName(String name);

    List<SubscriptionPlan> findByStatus(PlanStatus status);

    boolean existsByNameAndBillingCycleAndIsDeletedFalse(String name, BillingCycle billingCycle);
}
