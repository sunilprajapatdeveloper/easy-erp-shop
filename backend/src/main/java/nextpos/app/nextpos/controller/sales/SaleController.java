package nextpos.app.nextpos.controller.sales;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.service.interf.SaleService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    // Create Sale
    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody CreateSaleRequest request) {
        return new ResponseEntity<>(saleService.createSale(request), HttpStatus.CREATED);
    }

    // Get Sale by ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    // Get all Sales of logged-in user
    @GetMapping
    public ResponseEntity<List<SaleResponse>> getMySales() {
        return ResponseEntity.ok(saleService.getMySales());
    }

    // Get all Sales for current company (admin)
    @GetMapping("/company")
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    // Update Sale by ID
    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> updateSale(@PathVariable Long id,
            @Valid @RequestBody UpdateSaleRequest request) {
        return ResponseEntity.ok(saleService.updateSale(id, request));
    }

    // Delete Sale by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
