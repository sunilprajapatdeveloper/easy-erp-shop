package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    List<Adjustment> findByCreatedBy(Long createdBy);
    List<Adjustment> findByCompanyId(Long companyId);
}