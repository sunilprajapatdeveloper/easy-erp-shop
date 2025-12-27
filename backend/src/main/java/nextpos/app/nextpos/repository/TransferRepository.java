package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByCreatedBy(Long createdBy);

    List<Transfer> findByCompanyId(Long companyId);
}