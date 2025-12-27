package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

        Optional<ProductPrice> findByIdAndCompanyId(Long id, Long companyId);

        Optional<ProductPrice> findByProductIdAndWarehouseIdAndChannelAndCompanyId(Long productId,
                        Long warehouseId,
                        String channel,
                        Long companyId);

        /**
         * Finds a global (warehouse = null) price for the product/channel at company
         * scope.
         */
        Optional<ProductPrice> findByProductIdAndWarehouseIsNullAndChannelAndCompanyId(Long productId,
                        String channel,
                        Long companyId);

        List<ProductPrice> findAllByProductIdAndCompanyId(Long productId, Long companyId);

        List<ProductPrice> findAllByWarehouseIdAndCompanyId(Long warehouseId, Long companyId);

        List<ProductPrice> findAllByCompanyId(Long companyId);

        boolean existsByProductIdAndWarehouseIdAndChannelAndCompanyId(Long productId,
                        Long warehouseId,
                        String channel,
                        Long companyId);

        boolean existsByProductIdAndWarehouseIsNullAndChannelAndCompanyId(Long productId,
                        String channel,
                        Long companyId);

        Optional<ProductPrice> findByProductIdAndWarehouseIdAndCompanyId(
                        Long productId,
                        Long warehouseId,
                        Long companyId);

        List<ProductPrice> findAllByProductIdInAndWarehouseIdAndCompanyId(
                        List<Long> productIds, Long warehouseId, Long companyId);

        List<ProductPrice> findAllByProductIdInAndCompanyId(
                        List<Long> productIds, Long companyId);

}
