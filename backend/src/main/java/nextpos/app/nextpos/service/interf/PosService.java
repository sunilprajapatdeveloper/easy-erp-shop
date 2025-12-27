package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.SaleResponse;

public interface PosService {

    /**
     * Create a new sale based on selected products and customer
     */
    SaleResponse createSale(CreateSaleRequest request);

    /**
     * Get details of an existing sale
     */
    SaleResponse getSaleDetails(Long saleId);

    SaleResponse updateSale(Long id, UpdateSaleRequest request);

    /**
     * Generate printable PDF receipt
     */
    byte[] generateReceipt(Long saleId);
}
