package nextpos.app.nextpos.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(length = 100, nullable = false)
    private String firstname;

    @Column(length = 100)
    private String middleName;

    @Column(length = 100, nullable = false)
    private String lastname;

    @Column(unique = true, length = 100, nullable = false)
    private String username;

    @Column(unique = true, length = 150, nullable = false)
    private String email;

    @Column(unique = true, length = 15)
    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = true;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 255)
    private String profileImageUrl;

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

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 50)
    private String timezone;

    @Column(length = 10)
    private String language;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(length = 20)
    private String gender;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "position_title", length = 100)
    private String positionTitle;

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

    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /**
     * Default warehouse reference
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "default_warehouse_id")
    private Warehouse defaultWarehouse;

    /**
     * Relation with UserWarehouse → user can belong to multiple warehouses
     */
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

    /**
     * Utility method to add warehouse assignment
     */
    public void addUserWarehouse(UserWarehouse userWarehouse) {
        userWarehouses.add(userWarehouse);
        userWarehouse.setUser(this);
    }

    /**
     * Utility method to remove warehouse assignment
     */
    public void removeUserWarehouse(UserWarehouse userWarehouse) {
        userWarehouses.remove(userWarehouse);
        userWarehouse.setUser(null);
    }

    /**
     * Helper method to validate default warehouse
     */
    public boolean isDefaultWarehouseValid() {
        if (defaultWarehouse == null)
            return true; // optional
        return userWarehouses.stream()
                .anyMatch(uw -> uw.getWarehouse().equals(defaultWarehouse));
    }
}
