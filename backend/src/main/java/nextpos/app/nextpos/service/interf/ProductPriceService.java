package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductPriceRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductPriceRequest;
import nextpos.app.nextpos.model.dto.response.ProductPriceResponse;

import java.util.List;
import java.util.Optional;

public interface ProductPriceService {

    ProductPriceResponse createProductPrice(CreateProductPriceRequest request);

    ProductPriceResponse updateProductPrice(Long priceId, UpdateProductPriceRequest request);

    ProductPriceResponse getProductPriceById(Long priceId);

    ProductPriceResponse getByProductAndWarehouse(Long productId, Long warehouseId);

    Optional<ProductPriceResponse> findEffectivePrice(Long productId, Long warehouseId, String channel);

    List<ProductPriceResponse> listPricesByProduct(Long productId);

    List<ProductPriceResponse> listPricesByWarehouse(Long warehouseId);

    List<ProductPriceResponse> listAllPrices();

    void deleteProductPrice(Long priceId);
}
