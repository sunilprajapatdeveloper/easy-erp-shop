package nextpos.app.nextpos.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ExternalRateResponse {
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
}