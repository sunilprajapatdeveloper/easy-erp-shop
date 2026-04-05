package nextpos.app.nextpos.importexport.repository;

import nextpos.app.nextpos.importexport.entity.ImportError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportErrorRepository extends JpaRepository<ImportError, Long> {
    Page<ImportError> findByJobIdOrderByRowNumberAsc(Long jobId, Pageable pageable);

    List<ImportError> findByJobId(Long jobId);

    void deleteByJobId(Long jobId);
}