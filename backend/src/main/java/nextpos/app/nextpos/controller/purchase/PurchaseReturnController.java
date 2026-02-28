package nextpos.app.nextpos.controller.purchase;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseReturnResponse;
import nextpos.app.nextpos.service.interf.PurchaseReturnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-returns")
@RequiredArgsConstructor
public class PurchaseReturnController {

    private final PurchaseReturnService purchaseReturnService;

    /**
     * Create a new purchase return
     */
    @PostMapping
    public ResponseEntity<PurchaseReturnResponse> createPurchaseReturn(
            @RequestBody CreatePurchaseReturnRequest request) {
        PurchaseReturnResponse response = purchaseReturnService.createPurchaseReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get purchase return by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseReturnResponse> getPurchaseReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.getPurchaseReturnById(id));
    }

    /**
     * Get all purchase returns (optionally filter by supplier, warehouse)
     */
    @GetMapping
    public ResponseEntity<List<PurchaseReturnResponse>> getAllPurchaseReturns(
            @RequestParam(required = false) Long supplierId, @RequestParam(required = false) Long warehouseId) {
        List<PurchaseReturnResponse> responses = purchaseReturnService.getAllPurchaseReturns(supplierId, warehouseId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update an existing purchase return
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseReturnResponse> updatePurchaseReturn(
            @PathVariable Long id,
            @RequestBody UpdatePurchaseReturnRequest request) {
        return ResponseEntity.ok(purchaseReturnService.updatePurchaseReturn(id, request));
    }

    /**
     * Delete a purchase return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseReturn(@PathVariable Long id) {
        purchaseReturnService.deletePurchaseReturn(id);
        return ResponseEntity.noContent().build();
    }
}
