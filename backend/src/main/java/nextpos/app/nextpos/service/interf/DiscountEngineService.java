package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.entity.Sale;

public interface DiscountEngineService {
    void applyProductDiscounts(Sale sale);

    void applyOrderDiscount(Sale sale);
}