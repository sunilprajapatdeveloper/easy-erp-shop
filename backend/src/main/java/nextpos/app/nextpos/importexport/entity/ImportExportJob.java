package nextpos.app.nextpos.importexport.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_export_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportExportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_number")
    private Long jobNumber;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String module; // "Product", "Sale", etc.

    @Column(nullable = false, length = 10)
    private String type; // "IMPORT" or "EXPORT"

    @Column(nullable = false, length = 20)
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED

    @Column(name = "source_media_id", length = 36)
    private String sourceMediaId; // ID from Media entity

    @Column(name = "result_media_id", length = 36)
    private String resultMediaId;

    @Column(name = "error_media_id", length = 36)
    private String errorMediaId;

    @Column(name = "total_records")
    private Integer totalRecords;

    @Column(name = "processed_records")
    private Integer processedRecords;

    @Column(name = "success_records")
    private Integer successRecords;

    @Column(name = "error_records")
    private Integer errorRecords;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_summary", length = 1000)
    private String errorSummary;

    @Column(name = "options_json", columnDefinition = "TEXT")
    private String optionsJson; // JSON string for additional options

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}