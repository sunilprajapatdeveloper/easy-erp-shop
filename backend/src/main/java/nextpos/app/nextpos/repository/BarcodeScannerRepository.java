package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.BarcodeScanner;
import nextpos.app.nextpos.model.enums.ScannerStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarcodeScannerRepository extends JpaRepository<BarcodeScanner, Long> {

    Optional<BarcodeScanner> findByScannerIdAndCompanyId(String scannerId, Long companyId);

    List<BarcodeScanner> findByWarehouseIdAndCompanyId(Long warehouseId, Long companyId);

    List<BarcodeScanner> findByAssignedUserIdAndCompanyId(Long userId, Long companyId);

    List<BarcodeScanner> findByCompanyIdAndStatus(Long companyId, ScannerStatus status);

    @Query("SELECT bs FROM BarcodeScanner bs WHERE bs.companyId = :companyId AND bs.lastConnectedAt > :since")
    List<BarcodeScanner> findActiveScannersSince(@Param("companyId") Long companyId,
            @Param("since") LocalDateTime since);

    boolean existsByScannerIdAndCompanyId(String scannerId, Long companyId);
}