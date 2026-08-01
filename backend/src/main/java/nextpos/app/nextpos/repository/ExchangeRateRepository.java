package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ExchangeRate;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.ExchangeRateLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT e FROM ExchangeRate e WHERE e.id = :id AND (e.company.id = :companyId OR e.company IS NULL)")
    Optional<ExchangeRate> findAccessibleById(@Param("id") Long id, @Param("companyId") Long companyId);

    @Query("SELECT e FROM ExchangeRate e WHERE e.company.id = :companyId OR e.company IS NULL")
    List<ExchangeRate> findTenantAndGlobal(@Param("companyId") Long companyId);

    Optional<ExchangeRate> findByIdAndCompany_Id(Long id, Long companyId);

    // Find rate for a specific level and optional scope
    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrencyAndLevelAndCompanyAndWarehouse(
            Currency baseCurrency,
            Currency targetCurrency,
            ExchangeRateLevel level,
            Company company,
            Warehouse warehouse);

    List<ExchangeRate> findByCompany(Company company);

    List<ExchangeRate> findByWarehouse(Warehouse warehouse);

    List<ExchangeRate> findByLevel(ExchangeRateLevel level);
}
