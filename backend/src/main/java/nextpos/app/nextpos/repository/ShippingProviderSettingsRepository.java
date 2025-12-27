package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ShippingProviderSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingProviderSettingsRepository extends JpaRepository<ShippingProviderSettings, Long> {

    List<ShippingProviderSettings> findByCompanyId(Long companyId);

    List<ShippingProviderSettings> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId);

    Optional<ShippingProviderSettings> findByCompanyIdAndWarehouseIdAndId(Long companyId, Long warehouseId, Long id);
}