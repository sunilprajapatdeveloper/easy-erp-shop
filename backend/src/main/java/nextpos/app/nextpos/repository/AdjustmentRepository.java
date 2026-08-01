package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    Optional<Adjustment> findByIdAndCompanyId(Long id, Long companyId);
    List<Adjustment> findByCreatedBy(Long createdBy);
    List<Adjustment> findByCreatedByAndCompanyIdAndWarehouse_IdIn(Long createdBy, Long companyId,
            List<Long> warehouseIds);
    List<Adjustment> findByCompanyId(Long companyId);
    List<Adjustment> findByCompanyIdAndWarehouse_IdIn(Long companyId, List<Long> warehouseIds);
}
