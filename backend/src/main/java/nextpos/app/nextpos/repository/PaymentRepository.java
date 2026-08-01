package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByReferenceTypeAndReferenceId(PaymentSourceType referenceType, Long referenceId);

    List<Payment> findByCompanyId(Long companyId);

    Optional<Payment> findByIdempotencyKeyAndCompanyId(String idempotencyKey, Long companyId);

    Optional<Payment> findByIdAndCompanyId(Long id, Long companyId);

    List<Payment> findByReferenceTypeAndReferenceIdAndCompanyId(
            PaymentSourceType referenceType, Long referenceId, Long companyId);
}
