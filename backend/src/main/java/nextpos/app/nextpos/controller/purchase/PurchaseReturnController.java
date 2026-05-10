package nextpos.app.nextpos.controller.purchase;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseReturnRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseReturnResponse;
import nextpos.app.nextpos.service.interf.PurchaseReturnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-returns")
@RequiredArgsConstructor
public class PurchaseReturnController {

    private final PurchaseReturnService purchaseReturnService;

    @PostMapping
    public ResponseEntity<PurchaseReturnResponse> createPurchaseReturn(
            @Valid @RequestBody CreatePurchaseReturnRequest request) {
        return new ResponseEntity<>(purchaseReturnService.createPurchaseReturn(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseReturnResponse> getPurchaseReturn(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.getPurchaseReturnById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseReturnResponse>> getMyPurchaseReturns() {
        return ResponseEntity.ok(purchaseReturnService.getMyPurchaseReturns());
    }

    @GetMapping("/company")
    public ResponseEntity<List<PurchaseReturnResponse>> getAllPurchaseReturns() {
        return ResponseEntity.ok(purchaseReturnService.getAllPurchaseReturns());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseReturnResponse> updatePurchaseReturn(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePurchaseReturnRequest request) {
        return ResponseEntity.ok(purchaseReturnService.updatePurchaseReturn(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseReturn(@PathVariable Long id) {
        purchaseReturnService.deletePurchaseReturn(id);
        return ResponseEntity.noContent().build();
    }
}