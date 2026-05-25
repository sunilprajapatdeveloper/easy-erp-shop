package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductTax;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTaxRepository extends JpaRepository<ProductTax, Long> {

        Optional<ProductTax> findByIdAndCompanyId(Long id, Long companyId);

        List<ProductTax> findAllByProductIdAndCompanyId(Long productId, Long companyId);

        List<ProductTax> findAllByWarehouseIdAndCompanyId(Long warehouseId, Long companyId);

        List<ProductTax> findAllByCompanyId(Long companyId);

        boolean existsByProductIdAndWarehouseIdAndTaxCodeAndCompanyId(
                        Long productId, Long warehouseId, String taxCode, Long companyId);

        boolean existsByProductIdAndWarehouseIsNullAndTaxCodeAndCompanyId(
                        Long productId, String taxCode, Long companyId);

        Optional<ProductTax> findByProductIdAndWarehouseIdAndCompanyId(
                        Long productId,
                        Long warehouseId,
                        Long companyId);

        List<ProductTax> findAllByProductIdInAndWarehouseIdAndCompanyId(
                        List<Long> productIds, Long warehouseId, Long companyId);

        List<ProductTax> findAllByProductIdInAndCompanyId(
                        List<Long> productIds, Long companyId);

        /**
         * Returns the tax rule for a given product and warehouse, with fallback to
         * company‑wide (warehouse = null).
         * Only active taxes are considered.
         */
        @Query("SELECT pt FROM ProductTax pt " +
                        "WHERE pt.product = :product " +
                        "  AND (pt.warehouse = :warehouse OR pt.warehouse IS NULL) " +
                        "  AND pt.isActive = true " +
                        "ORDER BY CASE WHEN pt.warehouse IS NOT NULL THEN 0 ELSE 1 END")
        Optional<ProductTax> findByProductAndWarehouse(@Param("product") Product product,
                        @Param("warehouse") Warehouse warehouse);
}