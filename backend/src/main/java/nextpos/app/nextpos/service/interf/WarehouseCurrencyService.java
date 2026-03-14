package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseCurrencyResponse;

import java.util.List;

public interface WarehouseCurrencyService {

    WarehouseCurrencyResponse createWarehouseCurrency(Long companyId, Long warehouseId,
            CreateWarehouseCurrencyRequest request);

    WarehouseCurrencyResponse getWarehouseCurrency(Long id, Long companyId, Long warehouseId);

    WarehouseCurrencyResponse getDefaultWarehouseCurrency(Long companyId, Long warehouseId);

    List<WarehouseCurrencyResponse> listWarehouseCurrencies(Long companyId, Long warehouseId);

    WarehouseCurrencyResponse updateWarehouseCurrency(Long id, Long companyId, Long warehouseId,
            UpdateWarehouseCurrencyRequest request);

    void deleteWarehouseCurrency(Long id, Long companyId, Long warehouseId);
}
