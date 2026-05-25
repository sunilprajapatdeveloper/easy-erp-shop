package nextpos.app.nextpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nextpos.app.nextpos.model.entity.DiscountProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {
    boolean existsByDiscountIdAndProductId(Long discountId, Long productId);
}