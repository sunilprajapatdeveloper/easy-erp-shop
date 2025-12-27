package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

        Optional<ProductStock> findByProductIdAndWarehouseIdAndCompanyId(Long productId, Long warehouseId,
                        Long companyId);

        Optional<ProductStock> findByIdAndCompanyId(Long id, Long companyId);

        boolean existsByProductIdAndWarehouseIdAndCompanyId(Long productId, Long warehouseId, Long companyId);

        List<ProductStock> findAllByCompanyId(Long companyId);

        List<ProductStock> findAllByProductIdInAndCompanyId(
                        List<Long> productIds, Long companyId);

        List<ProductStock> findAllByProductIdInAndWarehouseIdAndCompanyId(
                        List<Long> productIds, Long warehouseId, Long companyId);
}
