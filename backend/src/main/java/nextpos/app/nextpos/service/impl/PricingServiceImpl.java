package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.service.interf.PricingService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final ProductPriceRepository productPriceRepository;

    @Override
    public void resolveProductPrices(Sale sale) {
        for (SaleProduct sp : sale.getProducts()) {
            Product product = sp.getProduct();

            // If a manual override is already set and > 0, keep it
            if (sp.getProductUnitPrice() != null &&
                    sp.getProductUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                continue;
            }

            // Find the best matching price for the current warehouse, customer group, etc.
            Optional<ProductPrice> bestPrice = findBestPrice(product, sale.getWarehouse(),
                    sale.getCustomer(), sale.getCurrency());

            BigDecimal price = bestPrice.map(ProductPrice::getPrice)
                    .orElse(BigDecimal.ZERO);

            sp.setProductUnitPrice(price);
        }
    }

    private Optional<ProductPrice> findBestPrice(Product product, Warehouse warehouse,
            Customer customer, Currency txnCurrency) {
        // Priority: warehouse-specific, then company-wide.
        // Filter by: isActive, validFrom/validTo, min/max quantity, customerGroup.
        // Return the first matching price in the requested currency.
        return productPriceRepository.findBestPriceForProduct(
                product, warehouse, txnCurrency);
    }
}