package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.VerificationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VerificationAttemptRepository extends JpaRepository<VerificationAttempt, UUID> {

    @Query("SELECT COUNT(a) FROM VerificationAttempt a WHERE a.attemptStatus = :status")
    Long countByAttemptStatus(@Param("status") String status);

    @Query("SELECT a FROM VerificationAttempt a WHERE a.attemptedAt < :threshold")
    List<VerificationAttempt> findByAttemptedAtBefore(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT COUNT(a) FROM VerificationAttempt a WHERE a.attemptedAt < :threshold")
    Long countByAttemptedAtBefore(@Param("threshold") LocalDateTime threshold);
}