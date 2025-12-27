package nextpos.app.nextpos.controller.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.enums.MediaType;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.interf.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MediaService mediaService;

    /**
     * Create new product for a company
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long createdBy,
            @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(companyId, createdBy, request));
    }

    /**
     * Get product by ID (company scoped)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(companyId, id));
    }

    /**
     * Get product by unique code (company scoped)
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponse> getProductByCode(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable String code) {
        return ResponseEntity.ok(productService.getProductByCode(companyId, code));
    }

    /**
     * Get product by barcode (company scoped)
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductResponse> getProductByBarcode(
            @RequestHeader("X-Company-Id") Long companyId,
            @PathVariable String barcode) {
        return ResponseEntity.ok(productService.getProductByBarcode(companyId, barcode));
    }

    /**
     * Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(companyId, updatedBy, id, request));
    }

    /**
     * Soft delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-User-Id") Long deletedBy,
            @PathVariable Long id) {
        productService.deleteProduct(companyId, deletedBy, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "includePrice", defaultValue = "false") boolean includePrice,
            @RequestParam(value = "includeStock", defaultValue = "false") boolean includeStock,
            @RequestParam(value = "includeTax", defaultValue = "false") boolean includeTax) {
        return ResponseEntity.ok(
                productService.getProducts(companyId, warehouseId, userId, includePrice, includeStock, includeTax));
    }

    @PostMapping("/{productId}/upload-images")
    public ResponseEntity<List<MediaResponse>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Company-Id") Long companyId) throws IOException {

        MediaUploadRequest request = MediaUploadRequest.builder()
                .companyId(companyId)
                .mediaType(MediaType.PRODUCT_IMAGE)
                .entityType("PRODUCT")
                .entityId(productId)
                .isPublic(true)
                .generateThumbnail(true)
                .build();

        List<MediaResponse> responses = mediaService.uploadFiles(files, request, userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<MediaResponse>> getProductImages(
            @PathVariable Long productId,
            @RequestHeader("X-Company-Id") Long companyId) {

        List<MediaResponse> responses = mediaService.getMediaByEntity(
                companyId, "PRODUCT", productId);
        return ResponseEntity.ok(responses);
    }
}
