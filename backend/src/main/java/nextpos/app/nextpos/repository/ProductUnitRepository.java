package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
    Optional<ProductUnit> findByIdAndCompanyId(Long id, Long companyId);

    // Load ProductUnit with creator info (optional, depending on use)
    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<ProductUnit> findById(@NonNull Long id);

    // Fetch all product units created by a specific user
    @NonNull
    List<ProductUnit> findAllByCreatedBy(@NonNull Long createdBy);

    @NonNull
    List<ProductUnit> findByCompanyId(@NonNull Long companyId);

    // Optionally, find by name if needed
    Optional<ProductUnit> findByName(@NonNull String name);

    Optional<ProductUnit> findByNameAndCompanyId(@NonNull String name, @NonNull Long companyId);

    boolean existsByIdAndCompanyId(Long id, Long companyId);
}
