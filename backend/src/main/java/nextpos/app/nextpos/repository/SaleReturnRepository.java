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
        @NonNull
        Optional<SaleReturn> findByReferenceNumber(@NonNull String referenceNumber);

        // Fetch all sale returns created by a specific user ID
        List<SaleReturn> findByCreatedBy(Long createdBy);

        // Fetch all sale returns for a company
        List<SaleReturn> findByCompanyId(Long companyId);

        // @EntityGraph(attributePaths = "products")
        // @NonNull
        // List<SaleReturn> findBySaleId(@NonNull Long saleId);

        /**
         * Sum of returned quantity for a specific product in a specific sale.
         * Returns 0 if no previous returns exist.
         */
        @Query("SELECT COALESCE(SUM(srp.returnQty), 0) " +
                        "FROM SaleReturn sr JOIN sr.products srp " +
                        "WHERE sr.originalSale.id = :saleId AND srp.product.id = :productId")
        int sumReturnedQtyBySaleAndProduct(@Param("saleId") Long saleId,
                        @Param("productId") Long productId);
}
