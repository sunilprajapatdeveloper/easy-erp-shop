package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.entity.Sale;

public interface StockValidationService {
    void validateAndDeduct(Sale sale);

    void reverseDeduction(Sale sale);
}