package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.exception.InsufficientStockException;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleProduct;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.StockValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockValidationServiceImpl implements StockValidationService {

    private final ProductStockService productStockService;

    @Override
    @Transactional
    public void validateAndDeduct(Sale sale) {
        Long warehouseId = sale.getWarehouse().getId();
        for (SaleProduct sp : sale.getProducts()) {
            Long productId = sp.getProduct().getId();
            int qty = sp.getQuantity();
            int available = productStockService.getAvailableQuantity(productId, warehouseId);
            if (available < qty) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product %s: need %d, have %d",
                                sp.getProduct().getName(), qty, available));
            }
        }
        // Deduct after full validation (all-or-nothing)
        for (SaleProduct sp : sale.getProducts()) {
            productStockService.adjustStock(sp.getProduct().getId(), warehouseId, -sp.getQuantity());
        }
    }

    @Override
    public void reverseDeduction(Sale sale) {
        Long warehouseId = sale.getWarehouse().getId();
        for (SaleProduct sp : sale.getProducts()) {
            productStockService.adjustStock(sp.getProduct().getId(), warehouseId, sp.getQuantity());
        }
    }
}