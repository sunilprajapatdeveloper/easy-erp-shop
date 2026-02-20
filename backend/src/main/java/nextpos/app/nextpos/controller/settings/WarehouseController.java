package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;
import nextpos.app.nextpos.service.interf.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Create a new warehouse
     */
    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(
            @Valid @RequestBody CreateWarehouseRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a single warehouse by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all warehouses
     */
    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        List<WarehouseResponse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    /**
     * Get warehouses created by a specific user
     */
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<WarehouseResponse>> getWarehousesByUser(@PathVariable Long userId) {
        List<WarehouseResponse> warehouses = warehouseService.findAllByCreatedBy(userId);
        return ResponseEntity.ok(warehouses);
    }

    /**
     * Update an existing warehouse
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWarehouseRequest request) {
        WarehouseResponse response = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a warehouse by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
