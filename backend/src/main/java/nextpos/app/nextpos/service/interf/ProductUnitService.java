package nextpos.app.nextpos.service.interf;

import java.util.List;

import nextpos.app.nextpos.model.dto.request.CreateProductUnitRequest;
import nextpos.app.nextpos.model.dto.response.ProductUnitResponse;

public interface ProductUnitService {
    ProductUnitResponse createProductUnit(CreateProductUnitRequest request);
    ProductUnitResponse getProductUnitById(Long id);
    List<ProductUnitResponse> findAllByCreatedBy(Long id);
    List<ProductUnitResponse> getAllProductUnits();
    ProductUnitResponse updateProductUnit(Long id, CreateProductUnitRequest request);
    void deleteProductUnit(Long id);
}
