package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.entity.Sale;
import java.math.BigDecimal;

public interface RoundingCalculationService {
    BigDecimal calculate(Sale sale);
}