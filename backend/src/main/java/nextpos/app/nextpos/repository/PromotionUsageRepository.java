package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Long> {

    @Query("SELECT COALESCE(SUM(pu.usageCount), 0) FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId")
    Integer getTotalUsageCount(@Param("promotionId") Long promotionId);

    @Query("SELECT COALESCE(SUM(pu.usageCount), 0) FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId AND pu.customerId = :customerId")
    Integer getCustomerUsageCount(@Param("promotionId") Long promotionId, @Param("customerId") Long customerId);

    Optional<PromotionUsage> findByPromotionIdAndSaleId(Long promotionId, Long saleId);
}