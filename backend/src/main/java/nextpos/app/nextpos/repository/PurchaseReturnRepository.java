package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PurchaseReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {

    // Optional: find all returns by supplier
    List<PurchaseReturn> findBySupplierId(Long supplierId);

    // Optional: find all returns by purchase
    List<PurchaseReturn> findByOriginalPurchaseId(Long purchaseId);

    // Optional: find all returns by warehouse
    List<PurchaseReturn> findByWarehouseId(Long warehouseId);

    @Query("SELECT COALESCE(SUM(prp.returnQty), 0) " +
            "FROM PurchaseReturnProduct prp " +
            "WHERE prp.purchaseReturn.originalPurchase.id = :purchaseId " +
            "AND prp.product.id = :productId")
    Integer sumReturnedQtyByPurchaseAndProduct(@Param("purchaseId") Long purchaseId,
            @Param("productId") Long productId);
}
