package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {

        List<Media> findByCompanyIdAndStoredFilename(Long companyId, String storedFilename);

        List<Media> findByCompanyIdAndEntityTypeAndEntityId(Long companyId,
                        String entityType,
                        Long entityId);

        List<Media> findByCompanyIdAndEntityType(Long companyId, String entityType);

        Optional<Media> findByCompanyIdAndStoragePath(Long companyId, String storagePath);

        Page<Media> findByCompanyId(Long companyId, Pageable pageable);

        Page<Media> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId, Pageable pageable);

        List<Media> findByCompanyIdAndIsTempTrueAndExpiresAtBefore(Long companyId, LocalDateTime expiryTime);

        @Query("SELECT m FROM Media m WHERE m.companyId = :companyId AND m.entityType = :entityType " +
                        "AND m.entityId IN :entityIds AND m.deletedAt IS NULL")
        List<Media> findByCompanyIdAndEntityTypeAndEntityIds(@Param("companyId") Long companyId,
                        @Param("entityType") String entityType,
                        @Param("entityIds") List<Long> entityIds);

        @Modifying
        @Query("UPDATE Media m SET m.deletedAt = CURRENT_TIMESTAMP WHERE m.id = :id AND m.companyId = :companyId")
        void softDelete(@Param("id") String id, @Param("companyId") Long companyId);

        @Query("SELECT SUM(m.fileSize) FROM Media m WHERE m.companyId = :companyId AND m.deletedAt IS NULL")
        Long getTotalStorageUsedByCompany(@Param("companyId") Long companyId);

        @Query("SELECT m FROM Media m WHERE m.companyId = :companyId AND m.deletedAt IS NULL " +
                        "ORDER BY m.accessedAt DESC NULLS LAST")
        List<Media> findRecentlyAccessed(@Param("companyId") Long companyId, Pageable pageable);

        // Add the missing count method
        long countByCompanyId(Long companyId);

        // Alternative: Find temp files for all companies
        @Query("SELECT m FROM Media m WHERE m.isTemp = true AND m.expiresAt < :expiryTime")
        List<Media> findTempFilesExpired(@Param("expiryTime") LocalDateTime expiryTime);
}