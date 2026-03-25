package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Sale;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @EntityGraph(attributePaths = "products")
    @NonNull
    List<Sale> findAll();

    @EntityGraph(attributePaths = "products")
    @NonNull
    Optional<Sale> findById(@NonNull Long id);

    @EntityGraph(attributePaths = "products")
    @NonNull
    Optional<Sale> findByReferenceNumber(@NonNull String referenceNumber);

    @EntityGraph(attributePaths = "products")
    List<Sale> findAllByCreatedBy(@NonNull Long userId);

    @EntityGraph(attributePaths = "products")
    List<Sale> findAllByCompanyId(@NonNull Long companyId);

    @Query("SELECT COALESCE(SUM(s.totalAmountTxnCurrency), 0) FROM Sale s WHERE s.companyId = :companyId AND s.saleStatus <> 'CANCELLED'")
    BigDecimal sumAllSalesByCompany(@Param("companyId") Long companyId);

    List<Sale> findTop5ByOrderByCreatedAtDesc();

    List<Sale> findTop5ByCompanyIdOrderByCreatedAtDesc(Long companyId);
}
