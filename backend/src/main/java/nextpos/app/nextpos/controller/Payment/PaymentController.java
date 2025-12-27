package nextpos.app.nextpos.controller.Payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.model.enums.PaymentStatus;
import nextpos.app.nextpos.service.interf.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/reference")
    public ResponseEntity<List<PaymentResponse>> getByReference(
            @RequestParam("type") PaymentSourceType type,
            @RequestParam("referenceId") Long referenceId) {
        List<PaymentResponse> responses = paymentService.getPaymentsByReference(type, referenceId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, PaymentStatus>> getPaymentStatus(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPayment(id);
        Map<String, PaymentStatus> statusResponse = new HashMap<>();
        statusResponse.put("status", payment.getStatus());
        return ResponseEntity.ok(statusResponse);
    }
}
