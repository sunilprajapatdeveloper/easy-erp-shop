package nextpos.app.nextpos.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nextpos.app.nextpos.model.entity.POSGeneralSettings;
import nextpos.app.nextpos.model.entity.Warehouse;

@Repository
public interface POSGeneralSettingsRepository extends JpaRepository<POSGeneralSettings, Long> {

    Optional<POSGeneralSettings> findByWarehouse(Warehouse warehouse);

    Optional<POSGeneralSettings> findByIdAndCompanyIdAndWarehouse_Id(Long id, Long companyId, Long warehouseId);

    Optional<POSGeneralSettings> findByCompany_IdAndWarehouse_Id(Long companyId, Long warehouseId);
}
