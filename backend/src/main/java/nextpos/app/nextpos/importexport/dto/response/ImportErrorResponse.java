package nextpos.app.nextpos.importexport.dto.response;

import lombok.Data;

@Data
public class ImportErrorResponse {
    private Long id;
    private Integer rowNumber;
    private String columnName;
    private String errorMessage;
    private String rawData;
}