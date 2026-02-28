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
            @Valid @RequestBody CreateProductPriceRequest request) {

        return ResponseEntity.ok(productPriceService.createProductPrice(request));
    }

    /**
     * Update an existing product price
     */
    @PutMapping("/{priceId}")
    public ResponseEntity<ProductPriceResponse> updateProductPrice(
            @PathVariable Long priceId,
            @Valid @RequestBody UpdateProductPriceRequest request) {

        return ResponseEntity.ok(productPriceService.updateProductPrice(priceId, request));
    }

    /**
     * Get a product price by ID
     */
    @GetMapping("/{priceId}")
    public ResponseEntity<ProductPriceResponse> getProductPriceById(
            @PathVariable Long priceId) {

        ProductPriceResponse response = productPriceService.getProductPriceById(priceId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all product prices for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPriceResponse>> getPricesByProduct(
            @PathVariable Long productId) {

        List<ProductPriceResponse> responses = productPriceService.listPricesByProduct(productId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all product prices for a warehouse
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductPriceResponse>> getPricesByWarehouse(
            @PathVariable Long warehouseId) {

        List<ProductPriceResponse> responses = productPriceService.listPricesByWarehouse(warehouseId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get a ProductPrice by product and warehouse
     */
    @GetMapping("/by-product-warehouse")
    public ResponseEntity<ProductPriceResponse> getByProductAndWarehouse(
            @RequestParam Long productId,
            @RequestParam Long warehouseId) {

        ProductPriceResponse response = productPriceService.getByProductAndWarehouse(productId, warehouseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product price
     */
    @DeleteMapping("/{priceId}")
    public ResponseEntity<Void> deleteProductPrice(
            @PathVariable Long priceId) {

        productPriceService.deleteProductPrice(priceId);
        return ResponseEntity.noContent().build();
    }
}
