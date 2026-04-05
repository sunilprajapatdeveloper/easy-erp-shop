package nextpos.app.nextpos.importexport.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class ExportRequest {
    private String module; // "Product", "Sale", etc.
    private String format; // "EXCEL", "CSV", "PDF"
    private Map<String, Object> filters; // e.g., { "dateFrom": "2023-01-01", "status": "ACTIVE" }
}