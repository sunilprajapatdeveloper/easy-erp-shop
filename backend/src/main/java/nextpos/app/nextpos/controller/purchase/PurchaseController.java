package nextpos.app.nextpos.controller.purchase;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePurchaseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePurchaseRequest;
import nextpos.app.nextpos.model.dto.response.PurchaseResponse;
import nextpos.app.nextpos.service.interf.PurchaseService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody CreatePurchaseRequest request) {
        return new ResponseEntity<>(purchaseService.createPurchase(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchase(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getMyPurchases() {
        return ResponseEntity.ok(purchaseService.getMyPurchases());
    }

    @GetMapping("/company")
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponse> updatePurchase(@PathVariable Long id,
            @Valid @RequestBody UpdatePurchaseRequest request) {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
