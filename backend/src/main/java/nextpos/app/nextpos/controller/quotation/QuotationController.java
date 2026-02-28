package nextpos.app.nextpos.controller.quotation;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateQuotationRequest;
import nextpos.app.nextpos.model.dto.response.QuotationResponse;
import nextpos.app.nextpos.service.interf.QuotationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    public ResponseEntity<QuotationResponse> createQuotation(@Valid @RequestBody CreateQuotationRequest request) {
        return new ResponseEntity<>(quotationService.createQuotation(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationResponse> getQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotationResponse> updateQuotation(@PathVariable Long id,
            @Valid @RequestBody CreateQuotationRequest request) {
        return ResponseEntity.ok(quotationService.updateQuotation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.noContent().build();
    }
}
