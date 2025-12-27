package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    WarehouseResponse createWarehouse(CreateWarehouseRequest request);

    WarehouseResponse getWarehouseById(Long id);

    List<WarehouseResponse> findAllByCreatedBy(Long userId);

    List<WarehouseResponse> getAllWarehouses();

    WarehouseResponse updateWarehouse(Long id, UpdateWarehouseRequest request);

    void deleteWarehouse(Long id);
}