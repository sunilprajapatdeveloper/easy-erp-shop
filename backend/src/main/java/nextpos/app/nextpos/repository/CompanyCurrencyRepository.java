package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.CompanyCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyCurrencyRepository extends JpaRepository<CompanyCurrency, Long> {

    Optional<CompanyCurrency> findByIdAndCompanyId(Long id, Long companyId);

    List<CompanyCurrency> findByCompanyId(Long companyId);

    boolean existsByCompanyIdAndDefaultCurrencyTrue(Long companyId);

    boolean existsByCompanyIdAndDefaultCurrencyTrueAndIdNot(Long companyId, Long id);

    @Modifying
    @Query("DELETE FROM CompanyCurrency c WHERE c.id = :id AND c.company.id = :companyId")
    int deleteByIdAndCompanyId(Long id, Long companyId);
}
