package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.entity.Sale;

public interface SaleCalculationService {
    /**
     * Performs all calculations (pricing, discounts, taxes, totals, currency
     * conversion)
     * on the given Sale entity. The Sale entity must already be populated with:
     * - customer, warehouse, currency, exchangeRate, products (with quantity and
     * unitPrice)
     * - manual discount fields or appliedDiscountId (if any)
     * - couponCode (if any)
     *
     * After this method, all calculated fields are set on the same Sale instance.
     */
    Sale calculate(Sale sale);
}