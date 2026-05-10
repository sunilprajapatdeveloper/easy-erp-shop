package nextpos.app.nextpos.controller.pos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.service.interf.PosService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pos")
@RequiredArgsConstructor
@Slf4j
public class PosController {

    private final PosService posService;

    /**
     * Create a new sale from the POS (products, discounts, taxes, customer, etc.)
     */
    @PostMapping("/sale")
    public ResponseEntity<SaleResponse> createSale(@RequestBody CreateSaleRequest saleRequest) {
        log.info("POS :: Creating sale for customerId = {}", saleRequest.getCustomerId());
        SaleResponse saleResponse = posService.createSale(saleRequest);
        return ResponseEntity.ok(saleResponse);
    }

    /**
     * Add a payment to an existing sale (e.g., card, cash, UPI)
     */
    @PostMapping("/sale/{saleId}/payment")
    public ResponseEntity<PaymentResponse> addPayment(
            @PathVariable Long saleId,
            @RequestBody CreatePaymentRequest paymentRequest) {
        log.info("POS :: Adding payment for saleId = {}, method = {}", saleId, paymentRequest.getPaymentMethod());
        PaymentResponse paymentResponse = posService.addPaymentToSale(saleId, paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    /**
     * Get sale details (including products and payments) by sale ID
     */
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long saleId) {
        log.info("POS :: Fetching sale details for ID = {}", saleId);
        SaleResponse saleResponse = posService.getSaleDetails(saleId);
        return ResponseEntity.ok(saleResponse);
    }

    /**
     * Generate a PDF receipt for a given sale (inline view in browser)
     */
    @GetMapping("/sale/{saleId}/receipt")
    public ResponseEntity<byte[]> printReceipt(@PathVariable Long saleId) {
        log.info("POS :: Generating PDF receipt for saleId = {}", saleId);
        byte[] pdfBytes = posService.generateReceipt(saleId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receipt_" + saleId + ".pdf")
                .body(pdfBytes);
    }

    /**
     * Update an existing sale by sale ID.
     */
    @PutMapping("/sale/{saleId}")
    public ResponseEntity<SaleResponse> updateSale(
            @PathVariable Long saleId,
            @RequestBody UpdateSaleRequest updateRequest) {
        log.info("POS :: Updating sale with ID = {}", saleId);
        updateRequest.setSource(SaleSource.POS);
        SaleResponse updatedSale = posService.updateSale(saleId, updateRequest);
        return ResponseEntity.ok(updatedSale);
    }
}
