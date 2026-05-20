package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.TaxSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaxSettingRepository extends JpaRepository<TaxSetting, Long> {

    List<TaxSetting> findAllByCompanyId(Long companyId);

    Optional<TaxSetting> findByIdAndCompanyId(Long id, Long companyId);

    Optional<TaxSetting> findByCompanyId(Long companyId);

    Optional<TaxSetting> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);

    Optional<TaxSetting> findByCompanyIdAndWarehouseIsNullAndActiveTrue(Long companyId);

    void deleteByIdAndCompanyId(Long id, Long companyId);
}