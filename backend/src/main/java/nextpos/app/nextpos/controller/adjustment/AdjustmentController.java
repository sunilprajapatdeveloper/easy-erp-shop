package nextpos.app.nextpos.controller.adjustment;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateAdjustmentRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentResponse;
import nextpos.app.nextpos.service.interf.AdjustmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/adjustments")
@RequiredArgsConstructor
public class AdjustmentController {

    private final AdjustmentService adjustmentService;

    @PostMapping
    public ResponseEntity<AdjustmentResponse> createAdjustment(@Valid @RequestBody CreateAdjustmentRequest request) {
        return new ResponseEntity<>(adjustmentService.createAdjustment(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdjustmentResponse> getAdjustment(@PathVariable Long id) {
        return ResponseEntity.ok(adjustmentService.getAdjustmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdjustmentResponse>> getMyAdjustments() {
        return ResponseEntity.ok(adjustmentService.getMyAdjustments());
    }

    @GetMapping("/company")
    public ResponseEntity<List<AdjustmentResponse>> getAllAdjustments() {
        return ResponseEntity.ok(adjustmentService.getAllAdjustments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdjustmentResponse> updateAdjustment(@PathVariable Long id,
            @Valid @RequestBody CreateAdjustmentRequest request) {
        return ResponseEntity.ok(adjustmentService.updateAdjustment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdjustment(@PathVariable Long id) {
        adjustmentService.deleteAdjustment(id);
        return ResponseEntity.noContent().build();
    }
}