package nextpos.app.nextpos.importexport.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class ImportRequest {
    private String module; // "Product", "Sale", etc.
    private MultipartFile file;
    private Map<String, Object> options; // e.g., { "warehouseId": 1, "overwriteExisting": false }
}