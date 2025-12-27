package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Company-scoped finders that exclude soft-deleted products.
     */
    Optional<Product> findByIdAndCompanyIdAndIsDeletedFalse(Long id, Long companyId);

    Optional<Product> findByCodeAndCompanyIdAndIsDeletedFalse(String code, Long companyId);

    Optional<Product> findByBarcodeAndCompanyIdAndIsDeletedFalse(String barcode, Long companyId);

    Optional<Product> findBySkuAndCompanyIdAndIsDeletedFalse(String sku, Long companyId);

    /**
     * Generic (not company-scoped) helpers used in some contexts.
     * These can be used when company scoping is applied elsewhere (e.g.
     * controller/service).
     */
    Optional<Product> findByCodeAndIsDeletedFalse(String code);

    Optional<Product> findByBarcodeAndIsDeletedFalse(String barcode);

    Optional<Product> findBySkuAndIsDeletedFalse(String sku);

    /**
     * Listing helpers
     */
    List<Product> findAllByCompanyIdAndIsDeletedFalse(Long companyId);

    List<Product> findAllByIsDeletedFalse();

    /**
     * Other helpers used by service (non-deleted)
     */
    List<Product> findAllByCreatedBy(Long createdBy);

    /**
     * Simple unique lookups (may return deleted results too; use the company-scoped
     * ones above
     * when you want to exclude deleted).
     */
    Optional<Product> findByCode(String code);

    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findBySku(String sku);

    /**
     * Existence checks (company scoped)
     */
    boolean existsByCodeAndCompanyIdAndIsDeletedFalse(String code, Long companyId);

    boolean existsByBarcodeAndCompanyIdAndIsDeletedFalse(String barcode, Long companyId);

    boolean existsBySkuAndCompanyIdAndIsDeletedFalse(String sku, Long companyId);
}
