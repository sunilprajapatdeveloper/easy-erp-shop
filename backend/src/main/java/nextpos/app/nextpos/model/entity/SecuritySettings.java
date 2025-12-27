package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "security_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class SecuritySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @Column(name = "enforce_two_factor_auth", nullable = false)
    @Builder.Default
    private Boolean enforceTwoFactorAuth = Boolean.FALSE;

    @Column(name = "enforce_password_policy", nullable = false)
    @Builder.Default
    private Boolean enforcePasswordPolicy = Boolean.TRUE;

    @Column(name = "restrict_ip_access", nullable = false)
    @Builder.Default
    private Boolean restrictIpAccess = Boolean.FALSE;

    @Column(name = "allowed_ip_ranges")
    private String allowedIpRanges;

    @Column(name = "max_login_attempts")
    @Builder.Default
    private Integer maxLoginAttempts = 5;

    @Column(name = "account_lock_duration_minutes")
    @Builder.Default
    private Integer accountLockDurationMinutes = 15;

    @Column(name = "session_timeout_minutes")
    @Builder.Default
    private Integer sessionTimeoutMinutes = 30;

    @Column(name = "password_expiry_days")
    @Builder.Default
    private Integer passwordExpiryDays = 90;

    @Column(name = "require_strong_passwords", nullable = false)
    @Builder.Default
    private Boolean requireStrongPasswords = Boolean.TRUE;

    @Column(name = "allow_device_trust", nullable = false)
    @Builder.Default
    private Boolean allowDeviceTrust = Boolean.FALSE;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
