package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;

public interface PosService {

    SaleResponse createSale(CreateSaleRequest request);

    SaleResponse getSaleDetails(Long saleId);

    SaleResponse updateSale(Long id, UpdateSaleRequest request);

    PaymentResponse addPaymentToSale(Long saleId, CreatePaymentRequest paymentRequest);

    byte[] generateReceipt(Long saleId);
}
