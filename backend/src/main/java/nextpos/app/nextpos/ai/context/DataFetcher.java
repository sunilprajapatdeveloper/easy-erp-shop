package nextpos.app.nextpos.ai.context;

import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;
import nextpos.app.nextpos.service.interf.ProductService;
import nextpos.app.nextpos.service.interf.SaleService;
import nextpos.app.nextpos.service.interf.WarehouseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetcher {
    private final ProductService productService;
    private final SaleService saleService;
    private final WarehouseService warehouseService;

    public DataFetcher(ProductService productService, SaleService saleService, WarehouseService warehouseService) {
        this.productService = productService;
        this.saleService = saleService;
        this.warehouseService = warehouseService;
    }

    public ProductResponse getProductById(Long id) {
        return productService.getProductById(id);
    }

    public List<SaleResponse> getRecentSales(String tenantId, int limit) {
        Long companyId = Long.parseLong(tenantId);
        return saleService.findRecentSalesByTenant(companyId, limit);
    }

    public WarehouseResponse getWarehouse(Long id) {
        return warehouseService.getWarehouseById(id);
    }
}