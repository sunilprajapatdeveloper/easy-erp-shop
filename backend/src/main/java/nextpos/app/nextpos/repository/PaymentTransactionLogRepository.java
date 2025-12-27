package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PaymentTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionLogRepository extends JpaRepository<PaymentTransactionLog, Long> {

    List<PaymentTransactionLog> findByPaymentId(Long paymentId);
}