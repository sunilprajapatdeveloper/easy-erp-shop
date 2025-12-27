package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PurchaseReturnProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseReturnProductRepository extends JpaRepository<PurchaseReturnProduct, Long> {

    @Modifying
    @Query("DELETE FROM PurchaseReturnProduct prp WHERE prp.purchaseReturn.id = :purchaseReturnId")
    void deleteByPurchaseReturnId(Long purchaseReturnId);
}
