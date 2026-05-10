package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseReturnResponse;

import java.util.List;

public interface PurchaseReturnService {

    PurchaseReturnResponse createPurchaseReturn(CreatePurchaseReturnRequest request);

    PurchaseReturnResponse getPurchaseReturnById(Long id);

    List<PurchaseReturnResponse> getMyPurchaseReturns();

    List<PurchaseReturnResponse> getAllPurchaseReturns();

    PurchaseReturnResponse updatePurchaseReturn(Long id, UpdatePurchaseReturnRequest request);

    void deletePurchaseReturn(Long id);
}