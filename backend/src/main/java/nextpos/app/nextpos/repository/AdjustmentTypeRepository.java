package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.AdjustmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentTypeRepository extends JpaRepository<AdjustmentType, Long> {
}