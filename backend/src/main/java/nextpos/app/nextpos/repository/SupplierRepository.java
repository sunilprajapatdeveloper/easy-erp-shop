package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Supplier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByCreatedBy(Long createdBy);

    List<Supplier> findByCompanyId(Long companyId);
    Optional<Supplier> findByIdAndCompanyId(Long id, Long companyId);
}
