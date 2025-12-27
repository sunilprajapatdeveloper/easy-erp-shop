package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductTaxRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductTaxRequest;
import nextpos.app.nextpos.model.dto.response.ProductTaxResponse;

import java.util.List;
import java.util.Optional;

public interface ProductTaxService {

    ProductTaxResponse createProductTax(Long companyId, Long createdBy, CreateProductTaxRequest request);

    ProductTaxResponse updateProductTax(Long companyId, Long updatedBy, Long taxId, UpdateProductTaxRequest request);

    ProductTaxResponse getProductTaxById(Long companyId, Long taxId);

    List<ProductTaxResponse> listTaxesByProduct(Long companyId, Long productId);

    List<ProductTaxResponse> listTaxesByWarehouse(Long companyId, Long warehouseId);

    List<ProductTaxResponse> listAllTaxes(Long companyId);

    void deleteProductTax(Long companyId, Long deletedBy, Long taxId);

    Optional<ProductTaxResponse> findEffectiveTax(Long companyId, Long productId, Long warehouseId, String taxCode);
}
