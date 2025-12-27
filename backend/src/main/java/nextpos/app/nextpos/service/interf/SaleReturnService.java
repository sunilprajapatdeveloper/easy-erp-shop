package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.response.SaleReturnResponse;

// import java.math.BigDecimal;
import java.util.List;

public interface SaleReturnService {
    SaleReturnResponse createSaleReturn(CreateSaleReturnRequest request);

    SaleReturnResponse getSaleReturnById(Long id);

    SaleReturnResponse updateSaleReturn(Long id, UpdateSaleReturnRequest request);

    void deleteSaleReturn(Long id);

    List<SaleReturnResponse> getMySaleReturns();

    List<SaleReturnResponse> getAllSaleReturns();

    // BigDecimal getTotalSalesReturn();
}
