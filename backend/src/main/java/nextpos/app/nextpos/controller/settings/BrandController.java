package nextpos.app.nextpos.controller.settings;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import nextpos.app.nextpos.model.dto.request.CreateBrandRequest;
import nextpos.app.nextpos.model.dto.response.BrandResponse;
import nextpos.app.nextpos.service.interf.BrandService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/brands")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BrandResponse>> getBrandsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(brandService.findAllByCreatedBy(userId));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long id,
            @Valid @RequestBody CreateBrandRequest request) {
        return ResponseEntity.ok(brandService.updateBrand(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}