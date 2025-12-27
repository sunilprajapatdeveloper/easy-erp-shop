package nextpos.app.nextpos.controller.sales;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleReturnRequest;
import nextpos.app.nextpos.model.dto.response.SaleReturnResponse;
import nextpos.app.nextpos.service.interf.SaleReturnService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-return")
@RequiredArgsConstructor
public class SaleReturnController {

    private final SaleReturnService saleReturnService;

    // Create Sale return
    @PostMapping
    public ResponseEntity<SaleReturnResponse> createSaleReturn(@Valid @RequestBody CreateSaleReturnRequest request) {
        return new ResponseEntity<>(saleReturnService.createSaleReturn(request), HttpStatus.CREATED);
    }

    // Get Sale Return by ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleReturnResponse> getSaleReturn(@PathVariable Long id) {
        return ResponseEntity.ok(saleReturnService.getSaleReturnById(id));
    }

    // Update Sale Return by ID
    @PutMapping("/{id}")
    public ResponseEntity<SaleReturnResponse> updateSaleReturn(@PathVariable Long id,
            @Valid @RequestBody UpdateSaleReturnRequest request) {
        return ResponseEntity.ok(saleReturnService.updateSaleReturn(id, request));
    }

    // Delete Sale Return by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleReturn(@PathVariable Long id) {
        saleReturnService.deleteSaleReturn(id);
        return ResponseEntity.noContent().build();
    }

    // Get all Sale Returns of logged-in user
    @GetMapping
    public ResponseEntity<List<SaleReturnResponse>> getMySaleReturns() {
        return ResponseEntity.ok(saleReturnService.getMySaleReturns());
    }

    // Get all Sale Returns for current company (admin)
    @GetMapping("/company")
    public ResponseEntity<List<SaleReturnResponse>> getAllSaleReturns() {
        return ResponseEntity.ok(saleReturnService.getAllSaleReturns());
    }
}