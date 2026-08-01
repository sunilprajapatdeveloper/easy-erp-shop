package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Brand;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByIdAndCompanyId(Long id, Long companyId);
    List<Brand> findAllByCompanyId(Long companyId);

    // Load Brand with creator info (optional, depending on use)
    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<Brand> findById(@NonNull Long id);

    // Fetch all product units created by a specific user
    @NonNull
    List<Brand> findAllByCreatedBy(@NonNull Long createdBy);

    // Optionally, find by name if needed
    @NonNull
    Optional<Brand> findByName(@NonNull String name);

    boolean existsByIdAndCompanyId(Long id, Long companyId);
}
