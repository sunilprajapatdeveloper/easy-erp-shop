package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductPriceRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductPriceRequest;
import nextpos.app.nextpos.model.dto.response.ProductPriceResponse;

import java.util.List;
import java.util.Optional;

public interface ProductPriceService {

    ProductPriceResponse createProductPrice(Long companyId, Long createdBy, CreateProductPriceRequest request);

    ProductPriceResponse updateProductPrice(Long companyId, Long updatedBy, Long priceId,
            UpdateProductPriceRequest request);

    ProductPriceResponse getProductPriceById(Long companyId, Long priceId);

    ProductPriceResponse getByProductAndWarehouse(Long companyId, Long productId, Long warehouseId);

    Optional<ProductPriceResponse> findEffectivePrice(Long companyId, Long productId, Long warehouseId, String channel);

    List<ProductPriceResponse> listPricesByProduct(Long companyId, Long productId);

    List<ProductPriceResponse> listPricesByWarehouse(Long companyId, Long warehouseId);

    List<ProductPriceResponse> listAllPrices(Long companyId);

    void deleteProductPrice(Long companyId, Long deletedBy, Long priceId);
}
