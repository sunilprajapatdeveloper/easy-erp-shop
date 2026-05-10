package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PromotionCustomerGroup;
import nextpos.app.nextpos.model.enums.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionCustomerGroupRepository extends JpaRepository<PromotionCustomerGroup, Long> {
    List<PromotionCustomerGroup> findByPromotionId(Long promotionId);

    @Query("SELECT pcg.customerGroup FROM PromotionCustomerGroup pcg WHERE pcg.promotion.id = :promotionId")
    List<CustomerGroup> findGroupsByPromotionId(@Param("promotionId") Long promotionId);

    void deleteByPromotionId(Long promotionId);
}