package nextpos.app.nextpos.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_attempts", indexes = {
        @Index(name = "idx_attempt_verification", columnList = "verificationId"),
        @Index(name = "idx_attempt_ip_time", columnList = "ipAddress,attemptedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID verificationId;

    @Column
    private String attemptedTokenHash;

    @Column(length = 45)
    private String ipAddress;

    @Column
    private String userAgent;

    @Column(length = 20)
    private String attemptStatus;

    @Column(nullable = false)
    private LocalDateTime attemptedAt;

    @Column
    private String failureReason;

    @PrePersist
    protected void onCreate() {
        if (attemptedAt == null) {
            attemptedAt = LocalDateTime.now();
        }
    }
}