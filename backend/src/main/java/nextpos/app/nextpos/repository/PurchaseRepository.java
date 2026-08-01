package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Purchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @EntityGraph(attributePaths = { "products", "supplier", "warehouse", "currency" })
    @NonNull
    Optional<Purchase> findById(@NonNull Long id);

    @EntityGraph(attributePaths = { "products", "supplier", "warehouse", "currency" })
    Optional<Purchase> findByIdAndCompanyId(Long id, Long companyId);

    @EntityGraph(attributePaths = { "products" })
    List<Purchase> findByCreatedBy(Long createdBy);

    @EntityGraph(attributePaths = { "products" })
    List<Purchase> findByCreatedByAndCompanyIdAndWarehouse_IdIn(Long createdBy, Long companyId,
            List<Long> warehouseIds);

    @EntityGraph(attributePaths = { "products" })
    List<Purchase> findByCompanyId(Long companyId);

    @EntityGraph(attributePaths = { "products" })
    List<Purchase> findByCompanyIdAndWarehouse_IdIn(Long companyId, List<Long> warehouseIds);

    @EntityGraph(attributePaths = { "products" })
    Optional<Purchase> findByReferenceNumber(String referenceNumber);
}
