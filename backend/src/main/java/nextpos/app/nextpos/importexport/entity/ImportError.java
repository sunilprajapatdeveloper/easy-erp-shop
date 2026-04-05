package nextpos.app.nextpos.importexport.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "import_errors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "column_name", length = 100)
    private String columnName;

    @Column(name = "error_message", nullable = false, length = 500)
    private String errorMessage;

    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData; // JSON of the row

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}