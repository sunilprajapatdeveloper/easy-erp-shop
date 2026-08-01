package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Load Customer with creator info
    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Optional<Customer> findById(@NonNull Long id);

    Optional<Customer> findByIdAndCompanyId(Long id, Long companyId);

    List<Customer> findAllByCompanyId(Long companyId);

    // Fetch all customers created by a specific user
    @NonNull
    List<Customer> findAllByCreatedBy(@NonNull Long createdBy);

    // Find by email
    @NonNull
    Optional<Customer> findByEmail(@NonNull String email);
    Optional<Customer> findByEmailAndCompanyId(String email, Long companyId);

    // Find by phone
    @NonNull
    Optional<Customer> findByPhone(@NonNull String phone);
    Optional<Customer> findByPhoneAndCompanyId(String phone, Long companyId);
}
