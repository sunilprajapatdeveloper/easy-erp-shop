package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Promotion;
import nextpos.app.nextpos.model.enums.PromotionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findByCompanyIdAndCodeAndIsActiveTrue(Long companyId, String code);

    @Query("SELECT p FROM Promotion p WHERE p.companyId = :companyId " +
            "AND p.isActive = true " +
            "AND p.startDate <= :now " +
            "AND (p.endDate IS NULL OR p.endDate >= :now) " +
            "AND (p.warehouseId IS NULL OR p.warehouseId = :warehouseId)")
    List<Promotion> findActiveByCompanyAndWarehouse(@Param("companyId") Long companyId,
            @Param("warehouseId") Long warehouseId,
            @Param("now") LocalDateTime now);

    @Query("SELECT p FROM Promotion p WHERE p.companyId = :companyId " +
            "AND p.type = :type " +
            "AND p.isActive = true " +
            "AND p.startDate <= :now " +
            "AND (p.endDate IS NULL OR p.endDate >= :now) " +
            "AND (p.warehouseId IS NULL OR p.warehouseId = :warehouseId)")
    List<Promotion> findActiveByType(@Param("companyId") Long companyId,
            @Param("warehouseId") Long warehouseId,
            @Param("type") PromotionType type,
            @Param("now") LocalDateTime now);

    Page<Promotion> findByCompanyId(Long companyId, Pageable pageable);
}