package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * Provides common audit fields and multi-tenant support.
 * - companyId ensures tenant isolation
 * - created/updated timestamps and user tracking
 * - optimistic locking with version
 */
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    /** ID of the user who created this record */
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    /** Timestamp of when the record was created */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** ID of the user who last updated this record */
    @Column(name = "updated_by")
    private Long updatedBy;

    /** Timestamp of the last update */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Tenant/company context */
    @Column(name = "company_id", updatable = false, nullable = false)
    private Long companyId;

    /** Optimistic locking version */
    @Version
    private Long version;

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
