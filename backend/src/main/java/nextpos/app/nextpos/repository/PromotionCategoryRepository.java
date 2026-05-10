package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PromotionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionCategoryRepository
        extends JpaRepository<PromotionCategory, PromotionCategory.PromotionCategoryId> {
    List<PromotionCategory> findByIdPromotionId(Long promotionId);

    @Query("SELECT pc.id.categoryId FROM PromotionCategory pc WHERE pc.id.promotionId = :promotionId")
    List<Long> findCategoryIdsByPromotionId(@Param("promotionId") Long promotionId);

    void deleteByIdPromotionId(Long promotionId);
}