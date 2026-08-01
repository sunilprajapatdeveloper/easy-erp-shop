package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PurchaseReturn;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {

        @EntityGraph(attributePaths = { "products", "supplier", "warehouse", "currency" })
        @NonNull
        Optional<PurchaseReturn> findById(@NonNull Long id);

        @EntityGraph(attributePaths = { "products", "supplier", "warehouse", "currency" })
        Optional<PurchaseReturn> findByIdAndCompanyId(Long id, Long companyId);

        @EntityGraph(attributePaths = { "products" })
        List<PurchaseReturn> findByCreatedBy(Long createdBy);

        @EntityGraph(attributePaths = { "products" })
        List<PurchaseReturn> findByCreatedByAndCompanyIdAndWarehouse_IdIn(Long createdBy, Long companyId,
                        List<Long> warehouseIds);

        @EntityGraph(attributePaths = { "products" })
        List<PurchaseReturn> findByCompanyId(Long companyId);

        @EntityGraph(attributePaths = { "products" })
        List<PurchaseReturn> findByCompanyIdAndWarehouse_IdIn(Long companyId, List<Long> warehouseIds);

        @Query("SELECT COALESCE(SUM(prp.quantity), 0) " +
                        "FROM PurchaseReturn pr JOIN pr.products prp " +
                        "WHERE pr.companyId = :companyId AND pr.originalPurchase.id = :purchaseId " +
                        "AND prp.product.id = :productId")
        int sumReturnedQtyByPurchaseAndProduct(@Param("purchaseId") Long purchaseId,
                        @Param("productId") Long productId, @Param("companyId") Long companyId);
}
