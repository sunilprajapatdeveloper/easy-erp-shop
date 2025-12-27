package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.CompanySubscription;
import nextpos.app.nextpos.model.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanySubscriptionRepository extends JpaRepository<CompanySubscription, Long> {

        @Query("SELECT cs FROM CompanySubscription cs " +
                        "WHERE cs.company.id = :companyId AND cs.status = nextpos.app.nextpos.model.enums.SubscriptionStatus.ACTIVE "
                        +
                        "AND cs.isDeleted = false")
        Optional<CompanySubscription> findActiveSubscriptionByCompanyId(Long companyId);

        @Query("SELECT cs.subscriptionPlan FROM CompanySubscription cs " +
                        "WHERE cs.company.id = :companyId AND cs.status = nextpos.app.nextpos.model.enums.SubscriptionStatus.ACTIVE "
                        +
                        "AND cs.isDeleted = false")
        Optional<SubscriptionPlan> findActivePlanByCompanyId(Long companyId);

        @Query("SELECT cs FROM CompanySubscription cs " +
                        "WHERE cs.company.id = :companyId AND cs.isDeleted = false ORDER BY cs.startDate DESC")
        Optional<CompanySubscription> findLatestSubscriptionByCompanyId(Long companyId);

        /** 🔹 Fetch all non-deleted subscriptions for a company */
        List<CompanySubscription> findByCompanyIdAndIsDeletedFalse(Long companyId);
}
