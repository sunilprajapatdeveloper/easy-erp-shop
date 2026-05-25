package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.ProductTax;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleProduct;
import nextpos.app.nextpos.model.enums.TaxApplicationOrder;
import nextpos.app.nextpos.model.enums.TaxInclusionType;
import nextpos.app.nextpos.model.dto.response.TaxSettingResponse;
import nextpos.app.nextpos.repository.ProductTaxRepository;
import nextpos.app.nextpos.service.interf.TaxCalculationService;
import nextpos.app.nextpos.service.interf.TaxSettingService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaxCalculationServiceImpl implements TaxCalculationService {

    private final ProductTaxRepository productTaxRepository;
    private final TaxSettingService taxSettingService; // NEW

    @Override
    public void calculateTaxes(Sale sale) {
        // Fetch the default tax setting for this sale’s warehouse (fallback to company)
        TaxSettingResponse taxSetting = taxSettingService.getActiveTaxSetting(
                sale.getWarehouse().getId());

        BigDecimal totalTax = BigDecimal.ZERO;

        for (SaleProduct sp : sale.getProducts()) {
            Optional<ProductTax> taxOpt = productTaxRepository.findByProductAndWarehouse(
                    sp.getProduct(), sale.getWarehouse());

            if (taxOpt.isEmpty()) {
                // No product‑specific tax → no tax
                BigDecimal lineGross = sp.getProductUnitPrice()
                        .multiply(BigDecimal.valueOf(sp.getQuantity()));
                sp.setLineNetAmount(lineGross);
                sp.setLineTaxAmount(BigDecimal.ZERO);
                sp.setLineGrossAmount(lineGross);
                sp.setTaxName("none");
                continue;
            }

            ProductTax tax = taxOpt.get();
            sp.setTaxName(tax.getTaxName());
            sp.setTaxCategory(tax.getTaxCategory());
            sp.setTaxRate(tax.getTaxRate());

            // Determine effective inclusion type
            TaxInclusionType inclusion = tax.getOverrideInclusionType();
            if (inclusion == null) {
                inclusion = taxSetting.getInclusionType(); // from company/warehouse default
            }

            // Determine effective application order
            TaxApplicationOrder order = tax.getOverrideApplicationOrder();
            if (order == null) {
                order = taxSetting.getApplicationOrder(); // from company/warehouse default
            }

            sp.setTaxInclusionType(inclusion);
            sp.setTaxApplicationOrder(order);

            BigDecimal quantity = BigDecimal.valueOf(sp.getQuantity());
            BigDecimal unitPrice = sp.getProductUnitPrice();
            BigDecimal lineTotalBeforeDiscount = unitPrice.multiply(quantity);
            BigDecimal lineDiscount = sp.getLineDiscountAmount() != null
                    ? sp.getLineDiscountAmount()
                    : BigDecimal.ZERO;

            BigDecimal taxableBase;
            if (order == TaxApplicationOrder.BEFORE_DISCOUNT) {
                taxableBase = lineTotalBeforeDiscount;
            } else {
                taxableBase = lineTotalBeforeDiscount.subtract(lineDiscount);
            }

            BigDecimal taxRate = tax.getTaxRate()
                    .divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP);
            BigDecimal net, taxAmount, gross;

            if (inclusion == TaxInclusionType.INCLUSIVE) {
                // Gross = taxableBase, tax = gross * rate / (1 + rate)
                gross = taxableBase;
                taxAmount = gross.multiply(taxRate)
                        .divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
                net = gross.subtract(taxAmount);
            } else { // EXCLUSIVE
                net = taxableBase;
                taxAmount = net.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
                gross = net.add(taxAmount);
            }

            sp.setLineNetAmount(net);
            sp.setLineTaxAmount(taxAmount);
            sp.setLineGrossAmount(gross);
            totalTax = totalTax.add(taxAmount);
        }

        sale.setTotalTaxAmount(totalTax);
    }
}