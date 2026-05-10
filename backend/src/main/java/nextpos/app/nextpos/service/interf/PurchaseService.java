package nextpos.app.nextpos.service.interf;

import java.util.List;
import nextpos.app.nextpos.model.dto.request.CreatePurchaseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseResponse;

public interface PurchaseService {

    PurchaseResponse createPurchase(CreatePurchaseRequest request);

    PurchaseResponse getPurchaseById(Long id);

    List<PurchaseResponse> getMyPurchases();

    List<PurchaseResponse> getAllPurchases();

    PurchaseResponse updatePurchase(Long id, UpdatePurchaseRequest request);

    void deletePurchase(Long id);
}