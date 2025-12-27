package nextpos.app.nextpos.controller.transfer;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateTransferRequest;
import nextpos.app.nextpos.model.dto.response.TransferResponse;
import nextpos.app.nextpos.service.interf.TransferService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody CreateTransferRequest request) {
        return new ResponseEntity<>(transferService.createTransfer(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getMyTransfer() {
        return ResponseEntity.ok(transferService.getMyTransfer());
    }

    @GetMapping("/company")
    public ResponseEntity<List<TransferResponse>> getAllTransfer() {
        return ResponseEntity.ok(transferService.getAllTransfer());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponse> updateTransfer(@PathVariable Long id,
            @Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.ok(transferService.updateTransfer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }
}
