package nextpos.app.nextpos.service.storage;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import nextpos.app.nextpos.model.enums.MediaType;

@Data
@Builder
public class StorageContext {
    private Long companyId;
    private Long warehouseId;
    private Long userId;
    private MediaType mediaType;
    private String entityType;
    private Long entityId;
    private boolean isPublic;
    private boolean generateThumbnail;
    private Map<String, Object> metadata;

    public String getStoragePath() {
        StringBuilder path = new StringBuilder();

        if (companyId != null) {
            path.append("company/").append(companyId).append("/");
        }

        if (warehouseId != null) {
            path.append("warehouse/").append(warehouseId).append("/");
        }

        if (mediaType != null) {
            path.append(mediaType.name().toLowerCase()).append("/");
        }

        if (entityType != null && entityId != null) {
            path.append(entityType.toLowerCase()).append("_").append(entityId).append("/");
        }

        return path.toString();
    }
}