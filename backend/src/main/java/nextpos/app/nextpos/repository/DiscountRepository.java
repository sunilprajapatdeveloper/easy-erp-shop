package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Page<Discount> findByCompanyId(Long companyId, Pageable pageable);

    Optional<Discount> findByCompanyIdAndCode(Long companyId, String code);

    List<Discount> findByCompanyIdAndIsActiveTrue(Long companyId);

    List<Discount> findByCompanyIdAndAutoApplyTrueAndIsActiveTrue(Long companyId);

    List<Discount> findByCompanyIdAndIsActiveTrueAndStartDateBeforeAndEndDateAfter(
            Long companyId,
            LocalDateTime now1,
            LocalDateTime now2);
}