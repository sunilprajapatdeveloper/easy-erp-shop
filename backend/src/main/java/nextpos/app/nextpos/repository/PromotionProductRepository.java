package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PromotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionProductRepository
        extends JpaRepository<PromotionProduct, PromotionProduct.PromotionProductId> {
    List<PromotionProduct> findByIdPromotionId(Long promotionId);

    @Query("SELECT pp.id.productId FROM PromotionProduct pp WHERE pp.id.promotionId = :promotionId")
    List<Long> findProductIdsByPromotionId(@Param("promotionId") Long promotionId);

    void deleteByIdPromotionId(Long promotionId);
}