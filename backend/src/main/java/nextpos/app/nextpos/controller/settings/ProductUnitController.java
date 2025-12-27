package nextpos.app.nextpos.controller.settings;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import nextpos.app.nextpos.model.dto.request.CreateProductUnitRequest;
import nextpos.app.nextpos.model.dto.response.ProductUnitResponse;
import nextpos.app.nextpos.service.interf.ProductUnitService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/units")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductUnitController {

    private final ProductUnitService productUnitService;

    @PostMapping
    public ResponseEntity<ProductUnitResponse> createProduct(@Valid @RequestBody CreateProductUnitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productUnitService.createProductUnit(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductUnitResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productUnitService.getProductUnitById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductUnitResponse>> getProductsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(productUnitService.findAllByCreatedBy(userId));
    }

    @GetMapping
    public ResponseEntity<List<ProductUnitResponse>> getAllProducts() {
        return ResponseEntity.ok(productUnitService.getAllProductUnits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUnitResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody CreateProductUnitRequest request) {
        return ResponseEntity.ok(productUnitService.updateProductUnit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productUnitService.deleteProductUnit(id);
        return ResponseEntity.noContent().build();
    }
}