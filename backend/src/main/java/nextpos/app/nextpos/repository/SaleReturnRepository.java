package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.SaleReturn;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface SaleReturnRepository extends JpaRepository<SaleReturn, Long> {

        @EntityGraph(attributePaths = "products")
        @NonNull
        List<SaleReturn> findAll();

        @EntityGraph(attributePaths = "products")
        @NonNull
        Optional<SaleReturn> findById(@NonNull Long id);

        @EntityGraph(attributePaths = "products")
        Optional<SaleReturn> findByIdAndCompanyId(Long id, Long companyId);

        @EntityGraph(attributePaths = "products")
        @NonNull
        Optional<SaleReturn> findByReferenceNumber(@NonNull String referenceNumber);

        List<SaleReturn> findByCreatedBy(Long createdBy);

        List<SaleReturn> findByCreatedByAndCompanyIdAndWarehouse_IdIn(Long createdBy, Long companyId,
                        List<Long> warehouseIds);

        List<SaleReturn> findByCompanyId(Long companyId);

        List<SaleReturn> findByCompanyIdAndWarehouse_IdIn(Long companyId, List<Long> warehouseIds);

        @Query("SELECT COALESCE(SUM(srp.quantity), 0) " +
                        "FROM SaleReturn sr JOIN sr.products srp " +
                        "WHERE sr.companyId = :companyId AND sr.originalSale.id = :saleId " +
                        "AND srp.product.id = :productId")
        int sumReturnedQtyBySaleAndProduct(@Param("saleId") Long saleId,
                        @Param("productId") Long productId, @Param("companyId") Long companyId);
}
