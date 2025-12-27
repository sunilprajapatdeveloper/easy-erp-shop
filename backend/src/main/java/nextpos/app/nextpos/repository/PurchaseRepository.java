package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Purchase;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCreatedBy(Long createdBy);

    List<Purchase> findByCompanyId(Long companyId);
}