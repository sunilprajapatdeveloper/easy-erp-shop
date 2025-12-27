package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.WarehouseCurrency;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseCurrencyRepository extends JpaRepository<WarehouseCurrency, Long> {

        @EntityGraph(attributePaths = { "currency" })
        @Query("SELECT wc FROM WarehouseCurrency wc WHERE wc.id = :id AND wc.company.id = :companyId AND wc.warehouse.id = :warehouseId")
        Optional<WarehouseCurrency> findByIdAndCompanyIdAndWarehouseId(@Param("id") Long id,
                        @Param("companyId") Long companyId,
                        @Param("warehouseId") Long warehouseId);

        List<WarehouseCurrency> findByCompany_IdAndWarehouse_Id(Long companyId, Long warehouseId);

        boolean existsByCompany_IdAndWarehouse_IdAndDefaultCurrencyTrue(Long companyId, Long warehouseId);

        @Modifying
        @Query("DELETE FROM WarehouseCurrency wc WHERE wc.id = :id AND wc.company.id = :companyId AND wc.warehouse.id = :warehouseId")
        int deleteByIdAndCompanyIdAndWarehouseId(@Param("id") Long id,
                        @Param("companyId") Long companyId,
                        @Param("warehouseId") Long warehouseId);

        @Query("SELECT CASE WHEN COUNT(wc) > 0 THEN true ELSE false END " +
                        "FROM WarehouseCurrency wc " +
                        "WHERE wc.company.id = :companyId AND wc.warehouse.id = :warehouseId " +
                        "AND wc.defaultCurrency = true AND wc.id <> :id")
        boolean existsByCompanyIdAndWarehouseIdAndDefaultCurrencyTrueAndIdNot(@Param("companyId") Long companyId,
                        @Param("warehouseId") Long warehouseId,
                        @Param("id") Long id);

        @EntityGraph(attributePaths = { "currency" })
        @Query("SELECT wc FROM WarehouseCurrency wc WHERE wc.company.id = :companyId AND wc.warehouse.id = :warehouseId AND wc.defaultCurrency = true")
        Optional<WarehouseCurrency> findDefaultByCompanyIdAndWarehouseId(
                        @Param("companyId") Long companyId,
                        @Param("warehouseId") Long warehouseId);
}
