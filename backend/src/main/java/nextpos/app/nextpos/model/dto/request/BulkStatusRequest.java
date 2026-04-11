package nextpos.app.nextpos.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BulkStatusRequest {
    private List<Long> ids;
    private String status;
}