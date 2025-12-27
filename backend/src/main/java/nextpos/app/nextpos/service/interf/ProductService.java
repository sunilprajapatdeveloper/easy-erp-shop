package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    /**
     * Create a new product for a company
     *
     * @param companyId company identifier
     * @param createdBy user id who is creating the product
     * @param request   product creation request payload
     * @return created product response
     */
    ProductResponse createProduct(Long companyId, Long createdBy, CreateProductRequest request);

    /**
     * Get product by ID scoped to company
     */
    ProductResponse getProductById(Long companyId, Long id);

    /**
     * Get product by code scoped to company
     */
    ProductResponse getProductByCode(Long companyId, String code);

    /**
     * Get product by barcode scoped to company
     */
    ProductResponse getProductByBarcode(Long companyId, String barcode);

    /**
     * Get all products for a company
     */
    List<ProductResponse> getAllProducts(Long companyId);

    /**
     * Update a product scoped to company
     *
     * @param companyId company identifier
     * @param updatedBy user id who is updating the product
     * @param id        product id
     * @param request   update payload
     * @return updated product response
     */
    ProductResponse updateProduct(Long companyId, Long updatedBy, Long id, UpdateProductRequest request);

    /**
     * Soft delete product scoped to company
     *
     * @param companyId company identifier
     * @param deletedBy user id who deletes the product
     * @param id        product id
     */
    void deleteProduct(Long companyId, Long deletedBy, Long id);

    /**
     * Adjust product stock (delegated to ProductStockService)
     *
     * @param productId     product id
     * @param quantityDelta quantity to adjust (+/-)
     */
    void adjustStock(Long productId, Integer quantityDelta);

    /**
     * Dynamic product listing based on filters.
     *
     * @param companyId    company identifier (mandatory)
     * @param warehouseId  optional warehouse id (filters to products linked to this
     *                     warehouse)
     * @param userId       optional user id (filters to products created by this
     *                     user)
     * @param includePrice include price details
     * @param includeStock include stock details
     * @param includeTax   include tax details
     * @return list of product responses (if only companyId given) OR
     *         warehouse-specific enriched response
     */
    List<ProductResponse> getProducts(Long companyId,
            Long warehouseId,
            Long userId,
            boolean includePrice,
            boolean includeStock,
            boolean includeTax);
}
