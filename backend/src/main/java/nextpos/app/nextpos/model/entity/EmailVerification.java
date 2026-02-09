package nextpos.app.nextpos.model.entity;

import lombok.*;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.model.enums.VerificationType;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verifications", indexes = {
        @Index(name = "idx_verification_token_hash", columnList = "tokenHash"),
        @Index(name = "idx_verification_email_status", columnList = "email,status"),
        @Index(name = "idx_verification_type_ref", columnList = "verificationType,referenceId")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 255)
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private VerificationType verificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VerificationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime verifiedAt;

    @Column
    private LocalDateTime lastAttemptAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer attemptCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxAttempts = 5;

    @Column(length = 50)
    private String referenceId;

    @Column(length = 50)
    private String referenceType;

    @Lob
    private String metadata;

    // Pre-persist callback
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = VerificationStatus.PENDING;
        }
    }
}