package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String username;

    @Column(unique = true, length = 150, nullable = false)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, length = 15)
    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "reset_token", length = 255)
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    @Column(name = "mfa_enabled")
    @Builder.Default
    private Boolean mfaEnabled = false;

    @Column(name = "mfa_secret", length = 255)
    private String mfaSecret;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "default_warehouse_id")
    private Warehouse defaultWarehouse;

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @Version
    private Long version;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserWarehouse> userWarehouses = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addUserWarehouse(UserWarehouse userWarehouse) {
        userWarehouses.add(userWarehouse);
        userWarehouse.setUser(this);
    }

    public void removeUserWarehouse(UserWarehouse userWarehouse) {
        userWarehouses.remove(userWarehouse);
        userWarehouse.setUser(null);
    }

    public boolean isDefaultWarehouseValid() {
        if (defaultWarehouse == null)
            return true;
        return userWarehouses.stream()
                .anyMatch(uw -> uw.getWarehouse().equals(defaultWarehouse));
    }
}
