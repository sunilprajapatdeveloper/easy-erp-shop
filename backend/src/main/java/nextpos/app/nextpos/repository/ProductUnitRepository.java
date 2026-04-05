package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {

    // Load ProductUnit with creator info (optional, depending on use)
    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<ProductUnit> findById(@NonNull Long id);

    // Fetch all product units created by a specific user
    @NonNull
    List<ProductUnit> findAllByCreatedBy(@NonNull Long createdBy);

    // Optionally, find by name if needed
    Optional<ProductUnit> findByName(@NonNull String name);

    boolean existsByIdAndCompanyId(Long id, Long companyId);
}