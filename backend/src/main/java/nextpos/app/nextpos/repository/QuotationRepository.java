package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Optional<Quotation> findByIdAndCompanyId(Long id, Long companyId);
}
