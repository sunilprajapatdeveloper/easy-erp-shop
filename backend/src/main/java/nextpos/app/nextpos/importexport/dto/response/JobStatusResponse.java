package nextpos.app.nextpos.importexport.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobStatusResponse {
    private Long id;
    private String status;
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer successRecords;
    private Integer errorRecords;
    private String errorSummary;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String resultUrl; // Signed URL for result file (export) or error file
    private String errorUrl; // Signed URL for error report
}