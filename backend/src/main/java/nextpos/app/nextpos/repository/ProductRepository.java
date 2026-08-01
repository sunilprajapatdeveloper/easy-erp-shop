package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    List<Product> findAllByIdInAndCompanyIdAndIsDeletedFalse(List<Long> ids, Long companyId);

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

    List<Product> findByCompanyIdAndIsDeletedFalseAndNameContainingIgnoreCaseOrCompanyIdAndIsDeletedFalseAndCodeContainingIgnoreCaseOrCompanyIdAndIsDeletedFalseAndSkuContainingIgnoreCase(
            Long nameCompanyId, String name, Long codeCompanyId, String code, Long skuCompanyId, String sku);

    @Query(value = """
            SELECT p.* FROM products p
            WHERE p.company_id = :companyId
              AND p.is_deleted = false
              AND p.search_vector::tsvector @@ to_tsquery('simple', :tsQuery)
            ORDER BY ts_rank(p.search_vector::tsvector, to_tsquery('simple', :tsQuery)) DESC
            OFFSET :offset LIMIT :limit
            """, nativeQuery = true)
    List<Product> searchByFullText(@Param("companyId") Long companyId,
            @Param("tsQuery") String tsQuery,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * Basic paginated query with optional search (LIKE) and sorting.
     */
    @Query("SELECT p FROM Product p WHERE p.companyId = :companyId AND p.isDeleted = false " +
            "AND (:search IS NULL OR :search = '' OR " +
            "     LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "     LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "     LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findAllByCompanyIdAndSearch(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);

    /**
     * Full‑text search using PostgreSQL tsvector (with GIN index) returning a Page.
     */
    @Query(value = """
            SELECT p.* FROM products p
            WHERE p.company_id = :companyId
              AND p.is_deleted = false
              AND (:search IS NULL OR :search = '' OR
                  p.search_vector @@ plainto_tsquery('simple', :search))
            ORDER BY p.id ASC
            """, countQuery = """
            SELECT COUNT(*) FROM products p
            WHERE p.company_id = :companyId
              AND p.is_deleted = false
              AND (:search IS NULL OR :search = '' OR
                  p.search_vector @@ plainto_tsquery('simple', :search))
            """, nativeQuery = true)
    Page<Product> searchFullText(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);

    /**
     * Find product by code and company ID (including soft-deleted products).
     * Used by import logic to detect deleted products and reactivate them.
     */
    Optional<Product> findByCodeAndCompanyId(String code, Long companyId);
}
