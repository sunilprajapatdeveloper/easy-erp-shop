package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.AdjustmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentTypeRepository extends JpaRepository<AdjustmentType, Long> {
    Optional<AdjustmentType> findByIdAndCompanyId(Long id, Long companyId);
    List<AdjustmentType> findAllByCompanyId(Long companyId);
}
