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
            @RequestParam Long warehouseId,
            @Valid @RequestBody CreateWarehouseCurrencyRequest request) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.createWarehouseCurrency(warehouseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> getWarehouseCurrency(
            @RequestParam Long warehouseId,
            @PathVariable Long id) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getWarehouseCurrency(id, warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/default")
    public ResponseEntity<WarehouseCurrencyResponse> getDefaultWarehouseCurrency(
            @RequestParam Long warehouseId) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getDefaultWarehouseCurrency(warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseCurrencyResponse>> listWarehouseCurrencies(
            @RequestParam Long warehouseId) {

        List<WarehouseCurrencyResponse> response = warehouseCurrencyService.listWarehouseCurrencies(warehouseId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> updateWarehouseCurrency(
            @RequestParam Long warehouseId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateWarehouseCurrencyRequest request) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.updateWarehouseCurrency(id, warehouseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseCurrency(
            @RequestParam Long warehouseId,
            @PathVariable Long id) {

        warehouseCurrencyService.deleteWarehouseCurrency(id, warehouseId);
        return ResponseEntity.noContent().build();
    }
}
