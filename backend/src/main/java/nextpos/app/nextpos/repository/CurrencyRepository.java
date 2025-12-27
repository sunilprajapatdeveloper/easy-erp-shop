package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Currency;
// import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

// import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    // Load Currency with creator info
    // @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<Currency> findById(@NonNull Long id);

    // // Fetch all currencies created by a specific user
    // @NonNull
    // List<Currency> findAllByCreatedBy(@NonNull Long createdBy);

    // Find currency by unique code
    @NonNull
    Optional<Currency> findByCode(@NonNull String code);

    // // Explicit alias for default currency lookup
    // @NonNull
    // default Optional<Currency> findDefaultCurrency(@NonNull Long companyId) {
    //     return findByCompanyIdAndIsBaseCurrencyTrue(companyId);
    // }

    // // Actual derived query method
    // @NonNull
    // Optional<Currency> findByCompanyIdAndIsBaseCurrencyTrue(@NonNull Long companyId);
}
