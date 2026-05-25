package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductPrice;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

        /**
         * Returns the best matching price for a given product, warehouse, and currency.
         * Priority: warehouse‑specific first, then company‑wide (warehouse is null).
         * Only active prices that are currently valid (if dates set).
         */
        @Query("SELECT pp FROM ProductPrice pp " +
                        "WHERE pp.product = :product " +
                        "  AND (pp.warehouse = :warehouse OR pp.warehouse IS NULL) " +
                        "  AND pp.currency = :currency " +
                        "  AND pp.isActive = true " +
                        "  AND (pp.validFrom IS NULL OR pp.validFrom <= CURRENT_TIMESTAMP) " +
                        "  AND (pp.validTo IS NULL OR pp.validTo >= CURRENT_TIMESTAMP) " +
                        "ORDER BY CASE WHEN pp.warehouse IS NOT NULL THEN 0 ELSE 1 END")
        Optional<ProductPrice> findBestPriceForProduct(@Param("product") Product product,
                        @Param("warehouse") Warehouse warehouse,
                        @Param("currency") Currency currency);
}