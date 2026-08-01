package nextpos.app.nextpos.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseCurrencyResponse;
import nextpos.app.nextpos.service.interf.WarehouseCurrencyService;
import nextpos.app.nextpos.security.context.UserContext;
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
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @Valid @RequestBody CreateWarehouseCurrencyRequest request) {

        Long companyId = authenticatedCompany(requestedCompanyId);
        WarehouseCurrencyResponse response = warehouseCurrencyService.createWarehouseCurrency(companyId, warehouseId,
                request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> getWarehouseCurrency(
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getWarehouseCurrency(id,
                authenticatedCompany(requestedCompanyId), warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/default")
    public ResponseEntity<WarehouseCurrencyResponse> getDefaultWarehouseCurrency(
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.getDefaultWarehouseCurrency(
                authenticatedCompany(requestedCompanyId),
                warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseCurrencyResponse>> listWarehouseCurrencies(
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId) {

        List<WarehouseCurrencyResponse> response = warehouseCurrencyService.listWarehouseCurrencies(
                authenticatedCompany(requestedCompanyId),
                warehouseId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseCurrencyResponse> updateWarehouseCurrency(
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateWarehouseCurrencyRequest request) {

        WarehouseCurrencyResponse response = warehouseCurrencyService.updateWarehouseCurrency(id,
                authenticatedCompany(requestedCompanyId),
                warehouseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseCurrency(
            @RequestHeader(value = "X-Company-Id", required = false) Long requestedCompanyId,
            @RequestHeader("X-Warehouse-Id") Long warehouseId,
            @PathVariable Long id) {

        warehouseCurrencyService.deleteWarehouseCurrency(id, authenticatedCompany(requestedCompanyId), warehouseId);
        return ResponseEntity.noContent().build();
    }

    private Long authenticatedCompany(Long requestedCompanyId) {
        Long companyId = UserContext.getCurrentCompanyId();
        if (requestedCompanyId != null && !companyId.equals(requestedCompanyId)) {
            throw new SecurityException("Company identifier does not match authenticated tenant");
        }
        return companyId;
    }
}
