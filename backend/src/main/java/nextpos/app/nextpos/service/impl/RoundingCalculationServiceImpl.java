package nextpos.app.nextpos.service.impl;

import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.service.interf.RoundingCalculationService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RoundingCalculationServiceImpl implements RoundingCalculationService {

    @Override
    public BigDecimal calculate(Sale sale) {
        // For a simple 2‑decimal rounding, the rounding amount is zero.
        // To implement currency‑specific rounding (e.g., CHF rounds to 0.05),
        // use the sale's currency settings.
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}