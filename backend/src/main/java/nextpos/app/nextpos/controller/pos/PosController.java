package nextpos.app.nextpos.controller.pos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.service.interf.PosService;
import nextpos.app.nextpos.service.interf.PaymentService;
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
    private final PaymentService paymentService;

    /**
     * Create a new sale from the POS (products, discounts, taxes, customer, etc.)
     */
    @PostMapping("/sale")
    public ResponseEntity<SaleResponse> createSale(@RequestBody CreateSaleRequest saleRequest) {
        log.info("POS :: Creating sale for customerId = {}", saleRequest.getCustomerId());

        // Create a new request based on the original, but with source = POS
        CreateSaleRequest posSaleRequest = CreateSaleRequest.builder()
                .date(saleRequest.getDate())
                .customerId(saleRequest.getCustomerId())
                .warehouseId(saleRequest.getWarehouseId())
                .products(saleRequest.getProducts())
                .orderTax(saleRequest.getOrderTax())
                .discount(saleRequest.getDiscount())
                .shippingCost(saleRequest.getShippingCost())
                .shipmentStatus(saleRequest.getShipmentStatus())
                .saleStatus(saleRequest.getSaleStatus())
                .source(SaleSource.POS)
                .note(saleRequest.getNote())
                .payments(saleRequest.getPayments())
                .currencyId(saleRequest.getCurrencyId())
                .exchangeRate(saleRequest.getExchangeRate())
                .build();

        SaleResponse saleResponse = posService.createSale(posSaleRequest);
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
        PaymentResponse paymentResponse = paymentService.processPayment(saleId, paymentRequest);
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
     * You can update products, discounts, taxes, customer, etc.
     */
    @PutMapping("/sale/{saleId}")
    public ResponseEntity<SaleResponse> updateSale(
            @PathVariable Long saleId,
            @RequestBody UpdateSaleRequest updateRequest) {
        log.info("POS :: Updating sale with ID = {}", saleId);

        // Ensure the source is POS
        updateRequest.setSource(SaleSource.POS);

        SaleResponse updatedSale = posService.updateSale(saleId, updateRequest);
        return ResponseEntity.ok(updatedSale);
    }

    // Future enhancements you may want to add here:
    // @PostMapping("/terminal/shift/open")
    // public ResponseEntity<...> openShift(...) { ... }

    // @PostMapping("/sale/{saleId}/refund")
    // public ResponseEntity<...> refundSale(...) { ... }
}
