package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductStockRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductStockRequest;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;

import java.util.List;

public interface ProductStockService {

    /**
     * Create a new product stock entry for a company.
     */
    ProductStockResponse createProductStock(Long companyId, Long userId, CreateProductStockRequest request);

    /**
     * Update an existing product stock entry.
     */
    ProductStockResponse updateProductStock(Long companyId, Long userId, UpdateProductStockRequest request);

    /**
     * Get a product stock entry by ID.
     */
    ProductStockResponse getProductStockById(Long companyId, Long id);

    /**
     * Get a product stock entry by product and warehouse.
     */
    ProductStockResponse getByProductAndWarehouse(Long companyId, Long productId, Long warehouseId);

    /**
     * List all product stock entries for a company.
     */
    List<ProductStockResponse> listStocksByCompany(Long companyId);

    /**
     * Adjust stock quantity atomically.
     */
    ProductStockResponse adjustStock(Long companyId, Long userId, Long productId, Long warehouseId, int delta);

    /**
     * Delete a product stock entry.
     */
    void deleteProductStock(Long companyId, Long userId, Long id);

    /**
     * Get current stock quantity for a product in a warehouse.
     */
    int getStock(Long companyId, Long productId, Long warehouseId);
}
