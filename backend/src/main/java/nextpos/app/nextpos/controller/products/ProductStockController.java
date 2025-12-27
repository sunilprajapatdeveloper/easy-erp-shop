package nextpos.app.nextpos.controller.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductStockRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductStockRequest;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.service.interf.ProductStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/product-stocks")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService productStockService;

    /**
     * Create a new ProductStock
     */
    @PostMapping
    public ResponseEntity<ProductStockResponse> createProductStock(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreateProductStockRequest request) {

        ProductStockResponse response = productStockService.createProductStock(companyId, createdBy, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<ProductStockResponse> updateProductStock(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long stockId,
            @Valid @RequestBody UpdateProductStockRequest request) {

        // set path variable ID explicitly
        request.setId(stockId);

        ProductStockResponse response = productStockService.updateProductStock(companyId, updatedBy, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Get a ProductStock by ID
     */
    @GetMapping("/{stockId}")
    public ResponseEntity<ProductStockResponse> getProductStockById(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long stockId) {

        ProductStockResponse response = productStockService.getProductStockById(companyId, stockId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a ProductStock by product and warehouse
     */
    @GetMapping("/by-product-warehouse")
    public ResponseEntity<ProductStockResponse> getByProductAndWarehouse(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestParam Long productId,
            @RequestParam Long warehouseId) {

        ProductStockResponse response = productStockService.getByProductAndWarehouse(companyId, productId, warehouseId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all ProductStocks for a company
     */
    @GetMapping
    public ResponseEntity<List<ProductStockResponse>> listStocksByCompany(
            @RequestHeader("X-Company-Id") Long companyId) {

        List<ProductStockResponse> stocks = productStockService.listStocksByCompany(companyId);
        return ResponseEntity.ok(stocks);
    }

    /**
     * Adjust stock quantity for a product in a warehouse
     */
    @PatchMapping("/adjust")
    public ResponseEntity<ProductStockResponse> adjustStock(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam int delta) {

        ProductStockResponse response = productStockService.adjustStock(companyId, updatedBy, productId, warehouseId,
                delta);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a ProductStock
     */
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteProductStock(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long stockId) {

        productStockService.deleteProductStock(companyId, userId, stockId);
        return ResponseEntity.noContent().build();
    }
}
