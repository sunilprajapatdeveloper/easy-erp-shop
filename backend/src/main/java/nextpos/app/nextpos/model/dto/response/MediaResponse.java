package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {
    private String id;
    private String originalFilename;
    private String storedFilename;
    private String url;
    private String thumbnailUrl;
    private Long fileSize;
    private String mimeType;
    private String extension;
    private String storageProvider;
    private Boolean isPublic;
    private String entityType;
    private Long entityId;
    private Long companyId;
    private Long warehouseId;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String metadata;
}