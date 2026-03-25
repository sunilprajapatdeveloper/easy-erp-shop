package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
// import nextpos.app.nextpos.model.dto.response.RecentSaleResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;

// import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SaleService {
    SaleResponse createSale(CreateSaleRequest request);

    SaleResponse getSaleById(Long id);

    List<SaleResponse> getMySales();

    List<SaleResponse> getAllSales();

    SaleResponse updateSale(Long id, UpdateSaleRequest request);

    void deleteSale(Long id);

    // BigDecimal getTotalSales();

    // List<RecentSaleResponse> getRecentSales();

    List<SaleResponse> findRecentSalesByTenant(Long tenantId, int limit);

    Map<String, Object> getSalesSummary(String period, Long warehouseId);
}
