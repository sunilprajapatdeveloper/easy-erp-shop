package nextpos.app.nextpos.importexport.repository;

import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportExportJobRepository extends JpaRepository<ImportExportJob, Long> {

    Page<ImportExportJob> findByCompanyIdAndModuleOrderByCreatedAtDesc(Long companyId, String module,
            Pageable pageable);

    Page<ImportExportJob> findByCompanyIdAndTypeAndStatus(Long companyId, String type, String status,
            Pageable pageable);

    Optional<ImportExportJob> findByIdAndCompanyId(Long id, Long companyId);

    @Modifying
    @Query("UPDATE ImportExportJob j SET j.status = :status, j.completedAt = CURRENT_TIMESTAMP WHERE j.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}