package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByIdAndCompanyId(Long id, Long companyId);
    List<Transfer> findByCreatedBy(Long createdBy);
    List<Transfer> findByCreatedByAndCompanyIdAndFromWarehouse_IdInAndToWarehouse_IdIn(
            Long createdBy, Long companyId, List<Long> fromWarehouseIds, List<Long> toWarehouseIds);

    List<Transfer> findByCompanyId(Long companyId);
    List<Transfer> findByCompanyIdAndFromWarehouse_IdInAndToWarehouse_IdIn(
            Long companyId, List<Long> fromWarehouseIds, List<Long> toWarehouseIds);
}
