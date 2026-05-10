package nextpos.app.nextpos.controller.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.BulkIdsRequest;
import nextpos.app.nextpos.model.dto.request.BulkStatusRequest;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.enums.MediaType;
import nextpos.app.nextpos.pagination.dto.ApiResponse;
import nextpos.app.nextpos.pagination.dto.PaginationRequest;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.model.entity.ProductStatus;
import nextpos.app.nextpos.security.context.UserContext;
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
            @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    /**
     * Get product by ID (company scoped)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id,
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "includePrice", defaultValue = "false") boolean includePrice,
            @RequestParam(value = "includeStock", defaultValue = "false") boolean includeStock,
            @RequestParam(value = "includeTax", defaultValue = "false") boolean includeTax) {
        return ResponseEntity
                .ok(productService.getProductById(id, warehouseId, includePrice, includeStock, includeTax));
    }

    /**
     * Get product by unique code (company scoped)
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponse> getProductByCode(
            @PathVariable String code) {
        return ResponseEntity.ok(productService.getProductByCode(code));
    }

    /**
     * Get product by barcode (company scoped)
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductResponse> getProductByBarcode(
            @PathVariable String barcode) {
        return ResponseEntity.ok(productService.getProductByBarcode(barcode));
    }

    /**
     * Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    /**
     * Soft delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "includePrice", defaultValue = "false") boolean includePrice,
            @RequestParam(value = "includeStock", defaultValue = "false") boolean includeStock,
            @RequestParam(value = "includeTax", defaultValue = "false") boolean includeTax,
            @RequestParam(value = "onlyComplete", defaultValue = "false") boolean onlyComplete) {
        return ResponseEntity.ok(
                productService.getProducts(warehouseId, userId, includePrice, includeStock, includeTax, onlyComplete));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(productService.searchProducts(query, page, size));
    }

    @GetMapping("/paginated")
    public ApiResponse<PaginationResponse<ProductResponse>> getProducts(
            @ModelAttribute PaginationRequest request) {
        PaginationResponse<ProductResponse> response = productService.getProducts(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/{productId}/upload-images")
    public ResponseEntity<List<MediaResponse>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {

        Long companyId = UserContext.getCurrentCompanyId();

        MediaUploadRequest request = MediaUploadRequest.builder()
                .companyId(companyId)
                .mediaType(MediaType.PRODUCT_IMAGE)
                .entityType("PRODUCT")
                .entityId(productId)
                .isPublic(true)
                .generateThumbnail(true)
                .build();

        List<MediaResponse> responses = mediaService.uploadFiles(files, request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<MediaResponse>> getProductImages(@PathVariable Long productId) {
        List<MediaResponse> responses = mediaService.getMediaByEntity("PRODUCT", productId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<Void> bulkDelete(@RequestBody BulkIdsRequest request) {
        productService.bulkDelete(request.getIds());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-update-status")
    public ResponseEntity<Void> bulkUpdateStatus(@RequestBody BulkStatusRequest request) {
        productService.bulkUpdateStatus(request.getIds(), ProductStatus.valueOf(request.getStatus()));
        return ResponseEntity.noContent().build();
    }
}
