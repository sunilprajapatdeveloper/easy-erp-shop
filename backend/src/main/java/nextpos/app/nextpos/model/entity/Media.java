package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media", indexes = {
        @Index(name = "idx_media_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_media_company", columnList = "company_id"),
        @Index(name = "idx_media_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_media_uploader", columnList = "uploaded_by"),
        @Index(name = "idx_media_storage", columnList = "storage_provider, storage_path")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false)
    private String storedFilename;

    @Column(name = "file_path", nullable = false, length = 2048)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Column(name = "storage_provider", nullable = false, length = 20)
    private String storageProvider; // LOCAL, S3, AZURE, GCS

    @Column(name = "storage_path", length = 2048)
    private String storagePath;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = true;

    @Column(name = "is_temp", nullable = false)
    @Builder.Default
    private Boolean isTemp = false;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON containing width, height, duration, etc.

    @Column(name = "thumbnail_path", length = 2048)
    private String thumbnailPath;

    @Column(name = "entity_type", length = 50)
    private String entityType; // PRODUCT, USER, COMPANY, WAREHOUSE, etc.

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Version
    private Long version;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.storedFilename == null) {
            this.storedFilename = UUID.randomUUID().toString() +
                    (this.extension != null ? "." + this.extension : "");
        }
    }
}