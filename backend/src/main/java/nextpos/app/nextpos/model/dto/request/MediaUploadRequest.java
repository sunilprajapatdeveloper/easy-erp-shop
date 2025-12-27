package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.MediaType;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadRequest {
    private Long companyId;
    private Long warehouseId;
    private MediaType mediaType;
    private String entityType;
    private Long entityId;
    @Builder.Default
    private boolean isPublic = true;
    @Builder.Default
    private boolean generateThumbnail = true;
    private Map<String, Object> metadata;
}