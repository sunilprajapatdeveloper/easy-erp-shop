package nextpos.app.nextpos.controller.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductTaxRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductTaxRequest;
import nextpos.app.nextpos.model.dto.response.ProductTaxResponse;
import nextpos.app.nextpos.service.interf.ProductTaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/product-taxes")
@RequiredArgsConstructor
public class ProductTaxController {

    private final ProductTaxService productTaxService;

    /**
     * Create a new product tax
     */
    @PostMapping
    public ResponseEntity<ProductTaxResponse> createProductTax(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreateProductTaxRequest request) {

        ProductTaxResponse response = productTaxService.createProductTax(companyId, createdBy, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing product tax
     */
    @PutMapping("/{taxId}")
    public ResponseEntity<ProductTaxResponse> updateProductTax(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long taxId,
            @Valid @RequestBody UpdateProductTaxRequest request) {

        ProductTaxResponse response = productTaxService.updateProductTax(companyId, updatedBy, taxId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a product tax by ID
     */
    @GetMapping("/{taxId}")
    public ResponseEntity<ProductTaxResponse> getProductTaxById(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long taxId) {

        ProductTaxResponse response = productTaxService.getProductTaxById(companyId, taxId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all product taxes for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductTaxResponse>> getTaxesByProduct(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long productId) {

        List<ProductTaxResponse> responses = productTaxService.listTaxesByProduct(companyId, productId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all product taxes for a warehouse
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductTaxResponse>> getTaxesByWarehouse(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long warehouseId) {

        List<ProductTaxResponse> responses = productTaxService.listTaxesByWarehouse(companyId, warehouseId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all product taxes for the company
     */
    @GetMapping
    public ResponseEntity<List<ProductTaxResponse>> getAllTaxes(
            @RequestHeader("X-Company-Id") Long companyId) {

        List<ProductTaxResponse> responses = productTaxService.listAllTaxes(companyId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Delete a product tax
     */
    @DeleteMapping("/{taxId}")
    public ResponseEntity<Void> deleteProductTax(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long deletedBy,
            @PathVariable Long taxId) {

        productTaxService.deleteProductTax(companyId, deletedBy, taxId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get effective tax for a product (optionally by warehouse)
     */
    @GetMapping("/effective")
    public ResponseEntity<ProductTaxResponse> getEffectiveTax(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam String taxCode) {

        ProductTaxResponse response = productTaxService
                .findEffectiveTax(companyId, productId, warehouseId, taxCode)
                .orElseThrow(() -> new IllegalArgumentException("Effective tax not found for given criteria"));

        return ResponseEntity.ok(response);
    }
}
