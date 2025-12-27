package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<Category> findById(@NonNull Long id);

    @NonNull
    List<Category> findAllByCreatedBy(@NonNull Long createdBy);

    @NonNull
    Optional<Category> findByCode(@NonNull String code);
}