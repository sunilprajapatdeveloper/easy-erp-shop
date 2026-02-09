package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.EmailVerification;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.model.enums.VerificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

        List<EmailVerification> findByEmailAndVerificationTypeAndStatus(
                        String email, VerificationType type, VerificationStatus status);

        Long countByEmailAndCreatedAtAfter(String email, LocalDateTime since);

        @Modifying
        @Query("UPDATE EmailVerification v SET v.status = 'EXPIRED' " +
                        "WHERE v.status = 'PENDING' AND v.expiresAt < :now")
        int expireStaleVerifications(@Param("now") LocalDateTime now);

        // Add these missing methods:
        List<EmailVerification> findByEmailAndStatus(String email, VerificationStatus status);

        Page<EmailVerification> findByEmailAndVerificationTypeAndStatus(
                        String email, VerificationType type, VerificationStatus status, Pageable pageable);

        Page<EmailVerification> findByEmailAndVerificationType(
                        String email, VerificationType type, Pageable pageable);

        Page<EmailVerification> findByEmail(String email, Pageable pageable);

        @Query("SELECT COUNT(v) FROM EmailVerification v WHERE v.status = :status")
        Long countByStatus(@Param("status") VerificationStatus status);

        @Query("SELECT COUNT(v) FROM EmailVerification v WHERE v.verificationType = :type")
        Long countByVerificationType(@Param("type") VerificationType type);

        @Query("SELECT COUNT(v) FROM EmailVerification v WHERE v.verificationType = :type AND v.status = :status")
        Long countByVerificationTypeAndStatus(@Param("type") VerificationType type,
                        @Param("status") VerificationStatus status);

        @Query("SELECT COUNT(v) FROM EmailVerification v WHERE v.status = :status AND v.createdAt < :date")
        Long countByStatusAndCreatedAtBefore(@Param("status") VerificationStatus status,
                        @Param("date") LocalDateTime date);

        @Query("SELECT COUNT(v) FROM EmailVerification v WHERE v.status = :status AND v.verifiedAt < :date")
        Long countByStatusAndVerifiedAtBefore(@Param("status") VerificationStatus status,
                        @Param("date") LocalDateTime date);

        @Query("SELECT v FROM EmailVerification v WHERE v.status = :status AND v.createdAt < :date")
        List<EmailVerification> findByStatusAndCreatedAtBefore(@Param("status") VerificationStatus status,
                        @Param("date") LocalDateTime date);

        @Query("SELECT v FROM EmailVerification v WHERE v.status = :status AND v.verifiedAt < :date")
        List<EmailVerification> findByStatusAndVerifiedAtBefore(@Param("status") VerificationStatus status,
                        @Param("date") LocalDateTime date);
}