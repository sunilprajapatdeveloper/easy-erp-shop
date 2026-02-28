package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductTaxRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductTaxRequest;
import nextpos.app.nextpos.model.dto.response.ProductTaxResponse;

import java.util.List;
import java.util.Optional;

public interface ProductTaxService {

    ProductTaxResponse createProductTax(CreateProductTaxRequest request);

    ProductTaxResponse updateProductTax(Long taxId, UpdateProductTaxRequest request);

    ProductTaxResponse getProductTaxById(Long taxId);

    List<ProductTaxResponse> listTaxesByProduct(Long productId);

    List<ProductTaxResponse> listTaxesByWarehouse(Long warehouseId);

    List<ProductTaxResponse> listAllTaxes();

    void deleteProductTax(Long taxId);

    Optional<ProductTaxResponse> findEffectiveTax(Long productId, Long warehouseId, String taxCode);
}
