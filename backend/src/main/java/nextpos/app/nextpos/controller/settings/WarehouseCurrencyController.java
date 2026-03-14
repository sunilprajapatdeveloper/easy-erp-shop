package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseCurrencyResponse;
import nextpos.app.nextpos.service.interf.WarehouseCurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouse-currencies")
@RequiredArgsConstructor
public class WarehouseCurrencyController {

    private final WarehouseCurrencyService warehouseCurrencyService;

    @PostMapping
    public ResponseEntity<WarehouseCurrencyResponse> createWarehouseCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @Valid @RequestBody CreateWarehouseCurrencyRequest request) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.createWarehouseCurrency(companyId, warehouseId,
                request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> getWarehouseCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getWarehouseCurrency(id, companyId, warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/default")
    public ResponseEntity<WarehouseCurrencyResponse> getDefaultWarehouseCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getDefaultWarehouseCurrency(companyId,
                warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseCurrencyResponse>> listWarehouseCurrencies(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {

        List<WarehouseCurrencyResponse> response = warehouseCurrencyService.listWarehouseCurrencies(companyId,
                warehouseId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> updateWarehouseCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateWarehouseCurrencyRequest request) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.updateWarehouseCurrency(id, companyId,
                warehouseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseCurrency(
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id) {

        warehouseCurrencyService.deleteWarehouseCurrency(id, companyId, warehouseId);
        return ResponseEntity.noContent().build();
    }
}
