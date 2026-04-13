package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.entity.ProductStatus;
import nextpos.app.nextpos.pagination.dto.PaginationRequest;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;

import java.util.List;

public interface ProductService {

    /**
     * Create a new product for a company
     *
     * @param request product creation request payload
     * @return created product response
     */
    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse createProduct(CreateProductRequest request, Long userId, Long companyId);

    /**
     * Get product by ID scoped to company
     */
    ProductResponse getProductById(Long id);

    ProductResponse getProductById(Long id, Long warehouseId, boolean includePrice, boolean includeStock, boolean includeTax);

    /**
     * Get product by code scoped to company
     */
    ProductResponse getProductByCode(String code);

    /**
     * Get product by barcode scoped to company
     */
    ProductResponse getProductByBarcode(String barcode);

    /**
     * Get all products for a company
     */
    List<ProductResponse> getAllProducts();

    List<ProductResponse> getAllProducts(Long companyId);

    /**
     * Update a product scoped to company
     *
     * @param id      product id
     * @param request update payload
     * @return updated product response
     */
    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    ProductResponse updateProduct(Long id, UpdateProductRequest request, Long userId, Long companyId);

    /**
     * Soft delete product scoped to company
     *
     * @param id product id
     */
    void deleteProduct(Long id);

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
    List<ProductResponse> getProducts(Long warehouseId,
            Long userId,
            boolean includePrice,
            boolean includeStock,
            boolean includeTax);

    List<ProductResponse> searchProducts(String query, int limit);

    List<ProductResponse> searchProducts(String query, int page, int size);

    PaginationResponse<ProductResponse> getProducts(PaginationRequest request);

    void bulkDelete(List<Long> ids);

    void bulkUpdateStatus(List<Long> ids, ProductStatus status);
}
