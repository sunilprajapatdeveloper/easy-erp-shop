package nextpos.app.nextpos.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BulkIdsRequest {
    private List<Long> ids;
}