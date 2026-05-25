package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.service.interf.CurrencyConversionService;
import nextpos.app.nextpos.service.interf.DiscountEngineService;
import nextpos.app.nextpos.service.interf.PricingService;
import nextpos.app.nextpos.service.interf.PromotionEngineIntegrationService;
import nextpos.app.nextpos.service.interf.RoundingCalculationService;
import nextpos.app.nextpos.service.interf.SaleCalculationService;
import nextpos.app.nextpos.service.interf.TaxCalculationService;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleCalculationServiceImpl implements SaleCalculationService {

    private final PricingService pricingService;
    private final DiscountEngineService discountEngineService;
    private final PromotionEngineIntegrationService promotionEngineIntegrationService;
    private final TaxCalculationService taxCalculationService;
    private final RoundingCalculationService roundingCalculationService;
    private final CurrencyConversionService currencyConversionService;

    @Override
    public Sale calculate(Sale sale) {
        // 1. Resolve product prices (override or default from price lists)
        pricingService.resolveProductPrices(sale);

        // 2. Apply automatic product‑level discounts (from discount rules)
        discountEngineService.applyProductDiscounts(sale);

        // 3. Apply manual / system order‑level discount
        discountEngineService.applyOrderDiscount(sale);

        // 4. Apply promotion (coupon)
        promotionEngineIntegrationService.applyPromotion(sale);

        // 5. Calculate taxes on products and total tax
        taxCalculationService.calculateTaxes(sale);

        // 6. Compute subtotal (already done during price resolution, but recalc if
        // needed)
        recalcSubtotal(sale);

        // 7. Compute totals
        BigDecimal subtotal = sale.getSubtotalAmountTxnCurrency();
        BigDecimal totalDiscount = sale.getOrderDiscount()
                .add(sale.getPromotionDiscountAmount() != null ? sale.getPromotionDiscountAmount() : BigDecimal.ZERO);
        // line discounts are already included in net amounts, but also added to
        // totalDiscount?
        // We'll accumulate line discounts as well (they are part of
        // totalDiscountAmount).
        BigDecimal lineDiscounts = sale.getProducts().stream()
                .map(sp -> sp.getLineDiscountAmount() != null ? sp.getLineDiscountAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalDiscount = totalDiscount.add(lineDiscounts);
        sale.setTotalDiscountAmount(totalDiscount);

        BigDecimal tax = sale.getTotalTaxAmount();
        BigDecimal shipping = sale.getShippingCost() != null ? sale.getShippingCost() : BigDecimal.ZERO;
        BigDecimal rounding = roundingCalculationService.calculate(sale);
        sale.setRoundingAmount(rounding);

        BigDecimal totalTxn = subtotal.subtract(totalDiscount).add(tax).add(shipping).add(rounding);
        sale.setTotalAmountTxnCurrency(totalTxn);
        sale.setGrandTotalTxnCurrency(totalTxn);

        BigDecimal paid = sale.getPaidAmountTxnCurrency() != null ? sale.getPaidAmountTxnCurrency() : BigDecimal.ZERO;
        sale.setDueAmountTxnCurrency(totalTxn.subtract(paid));

        // 8. Convert to base currency
        currencyConversionService.convert(sale);

        return sale;
    }

    private void recalcSubtotal(Sale sale) {
        BigDecimal subtotal = sale.getProducts().stream()
                .map(p -> p.getProductUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setSubtotalAmountTxnCurrency(subtotal);
    }
}