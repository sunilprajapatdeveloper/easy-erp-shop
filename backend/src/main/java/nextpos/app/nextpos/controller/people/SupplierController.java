package nextpos.app.nextpos.controller.people;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSupplierRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSupplierRequest;
import nextpos.app.nextpos.model.dto.response.SupplierResponse;
import nextpos.app.nextpos.service.interf.SupplierService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplier(@PathVariable Long id) {
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getMySuppliers() {
        return ResponseEntity.ok(supplierService.getMySuppliers());
    }

    @GetMapping("/company")
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id,
            @Valid @RequestBody UpdateSupplierRequest request) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id,
            @RequestParam("deletedBy") Long deletedByUserId) {
        supplierService.deleteSupplier(id, deletedByUserId);
        return ResponseEntity.noContent().build();
    }
}
