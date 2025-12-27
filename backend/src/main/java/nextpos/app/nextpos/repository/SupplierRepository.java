package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByCreatedBy(Long createdBy);

    List<Supplier> findByCompanyId(Long companyId);
}