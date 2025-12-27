package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

}