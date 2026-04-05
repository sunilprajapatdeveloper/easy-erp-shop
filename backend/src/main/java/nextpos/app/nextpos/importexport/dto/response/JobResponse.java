package nextpos.app.nextpos.importexport.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponse {
    private Long id;
    private Long jobNumber;
    private String module;
    private String type;
    private String status;
    private String sourceMediaId;
    private String resultMediaId;
    private String errorMediaId;
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer successRecords;
    private Integer errorRecords;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorSummary;
    private String optionsJson;
    private LocalDateTime createdAt;
}