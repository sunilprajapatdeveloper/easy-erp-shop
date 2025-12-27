package nextpos.app.nextpos.controller.adjustment;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateAdjustmentTypeRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentTypeResponse;
import nextpos.app.nextpos.service.interf.AdjustmentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adjustment-types")
@RequiredArgsConstructor
public class AdjustmentTypeController {
    private final AdjustmentTypeService adjustmentTypeService;

    @PostMapping
    public ResponseEntity<AdjustmentTypeResponse> create(@RequestBody CreateAdjustmentTypeRequest request) {
        return ResponseEntity.ok(adjustmentTypeService.createAdjustmentType(request));
    }

    @GetMapping
    public ResponseEntity<List<AdjustmentTypeResponse>> getAll() {
        return ResponseEntity.ok(adjustmentTypeService.getAllAdjustmentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdjustmentTypeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adjustmentTypeService.getAdjustmentTypeById(id));
    }
}