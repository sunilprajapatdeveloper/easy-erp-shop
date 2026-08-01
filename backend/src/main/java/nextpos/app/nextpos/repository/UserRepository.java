package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "role")
    @NonNull
    List<User> findAll();

    @EntityGraph(attributePaths = "role")
    @NonNull
    Optional<User> findById(@NonNull Long id);

    @EntityGraph(attributePaths = { "role", "role.permissions", "defaultWarehouse", "userWarehouses", "userWarehouses.warehouse" })
    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    @EntityGraph(attributePaths = { "role", "role.permissions", "defaultWarehouse", "userWarehouses", "userWarehouses.warehouse" })
    @NonNull
    Optional<User> findByPhone(@NonNull String phone);

    @Query("SELECT u FROM User u WHERE u.createdBy = :creatorId OR u.createdBy IN (SELECT u2.id FROM User u2 WHERE u2.createdBy = :creatorId)")
    @EntityGraph(attributePaths = "role")
    List<User> findAllAssociatedUsers(@Param("creatorId") Long creatorId);

    Optional<User> findByIdAndCompanyId(Long id, Long companyId);

    boolean existsByCompanyId(Long companyId);
}
