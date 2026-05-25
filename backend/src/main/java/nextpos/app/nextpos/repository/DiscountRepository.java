package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

        /**
         * Returns product-level discounts active for the given company, warehouse, and
         * date.
         * The warehouseId can be null to match discounts that are not restricted to a
         * specific warehouse.
         */
        @Query("SELECT d FROM Discount d WHERE d.scope = 'PRODUCT' AND d.isActive = true " +
                        "AND d.companyId = :companyId " +
                        "AND (d.warehouseId IS NULL OR d.warehouseId = :warehouseId) " +
                        "AND (d.startDate IS NULL OR d.startDate <= :date) " +
                        "AND (d.endDate IS NULL OR d.endDate >= :date)")
        List<Discount> findActiveProductDiscounts(@Param("companyId") Long companyId,
                        @Param("warehouseId") Long warehouseId,
                        @Param("date") LocalDate date);
}