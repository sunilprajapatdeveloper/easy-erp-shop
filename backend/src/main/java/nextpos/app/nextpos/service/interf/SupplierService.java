package nextpos.app.nextpos.service.interf;

import java.util.List;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSupplierRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSupplierRequest;
import nextpos.app.nextpos.model.dto.response.SupplierResponse;

public interface SupplierService {

    SupplierResponse createSupplier(CreateSupplierRequest request);

    SupplierResponse getSupplierById(Long id);

    List<SupplierResponse> getMySuppliers();

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse updateSupplier(Long id, UpdateSupplierRequest request);

    void deleteSupplier(Long id);
}
