package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Warehouse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    /**
     * Find warehouse by ID, company, and soft-delete flag.
     * Currency is eagerly loaded to avoid N+1 issue.
     */
    @EntityGraph(attributePaths = "currency")
    @NonNull
    Optional<Warehouse> findByIdAndCompanyIdAndIsDeletedFalse(@NonNull Long id, @NonNull Long companyId);

    /**
     * Find all warehouses for a given company, excluding soft-deleted ones.
     */
    @NonNull
    List<Warehouse> findAllByCompanyIdAndIsDeletedFalse(@NonNull Long companyId);

    /**
     * Find all warehouses created by a specific user, scoped by company, excluding
     * soft-deleted ones.
     */
    @NonNull
    List<Warehouse> findAllByCreatedByAndCompanyIdAndIsDeletedFalse(@NonNull Long createdBy, @NonNull Long companyId);

    /**
     * Find a warehouse by name, scoped by company, excluding soft-deleted ones.
     */
    @NonNull
    Optional<Warehouse> findByNameAndCompanyIdAndIsDeletedFalse(@NonNull String name, @NonNull Long companyId);

    /**
     * Find default warehouse for a company.
     */
    @NonNull
    Optional<Warehouse> findByCompanyIdAndIsDefaultTrueAndIsDeletedFalse(@NonNull Long companyId);
}
