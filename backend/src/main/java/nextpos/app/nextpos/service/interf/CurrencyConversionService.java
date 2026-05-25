package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.entity.Sale;

public interface CurrencyConversionService {
    void convert(Sale sale);
}