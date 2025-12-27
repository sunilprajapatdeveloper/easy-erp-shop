package nextpos.app.nextpos.controller.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductPriceRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductPriceRequest;
import nextpos.app.nextpos.model.dto.response.ProductPriceResponse;
import nextpos.app.nextpos.service.interf.ProductPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/product-prices")
@RequiredArgsConstructor
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    /**
     * Create a new product price
     */
    @PostMapping
    public ResponseEntity<ProductPriceResponse> createProductPrice(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreateProductPriceRequest request) {

        return ResponseEntity.ok(productPriceService.createProductPrice(companyId, createdBy, request));
    }

    /**
     * Update an existing product price
     */
    @PutMapping("/{priceId}")
    public ResponseEntity<ProductPriceResponse> updateProductPrice(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long priceId,
            @Valid @RequestBody UpdateProductPriceRequest request) {

        return ResponseEntity.ok(productPriceService.updateProductPrice(companyId, updatedBy, priceId, request));
    }

    /**
     * Get a product price by ID
     */
    @GetMapping("/{priceId}")
    public ResponseEntity<ProductPriceResponse> getProductPriceById(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long priceId) {

        ProductPriceResponse response = productPriceService.getProductPriceById(companyId, priceId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all product prices for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPriceResponse>> getPricesByProduct(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long productId) {

        List<ProductPriceResponse> responses = productPriceService.listPricesByProduct(companyId, productId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all product prices for a warehouse
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductPriceResponse>> getPricesByWarehouse(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long warehouseId) {

        List<ProductPriceResponse> responses = productPriceService.listPricesByWarehouse(companyId, warehouseId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get a ProductPrice by product and warehouse
     */
    @GetMapping("/by-product-warehouse")
    public ResponseEntity<ProductPriceResponse> getByProductAndWarehouse(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestParam Long productId,
            @RequestParam Long warehouseId) {

        ProductPriceResponse response = productPriceService.getByProductAndWarehouse(companyId, productId, warehouseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product price
     */
    @DeleteMapping("/{priceId}")
    public ResponseEntity<Void> deleteProductPrice(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long deletedBy,
            @PathVariable Long priceId) {

        productPriceService.deleteProductPrice(companyId, deletedBy, priceId);
        return ResponseEntity.noContent().build();
    }
}
