package nextpos.app.nextpos.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.RazorpayConfig;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.UpiPaymentInitiateResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl {

    private final RazorpayConfig razorpayConfig;

    /**
     * Initiates a UPI payment order using Razorpay and returns payment instructions
     * (UPI deep link or QR).
     *
     * @param request The incoming payment request
     * @return A UPI payment initiation response with Razorpay orderId and UPI link
     */
    public UpiPaymentInitiateResponse initiateUpiPayment(CreatePaymentRequest request) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayConfig.getKey(), razorpayConfig.getSecret());

            JSONObject options = new JSONObject();
            options.put("amount", request.getAmountTxnCurrency().multiply(BigDecimal.valueOf(100)).intValue()); // Convert
                                                                                                                // to
                                                                                                                // paise
            options.put("currency", request.getCurrencyCode());
            options.put("receipt", request.getReferenceNumber());
            options.put("payment_capture", 1);
            options.put("method", "upi");

            Order order = client.orders.create(options);

            // UPI link format: https://razorpay.com/payment-link/<orderId> OR custom
            String upiLink = "upi://pay?pa=razorpay@icici&pn=NextPOS&tr=" + order.get("id") +
                    "&am=" + request.getAmountTxnCurrency() + "&cu=" + request.getCurrencyCode();

            return UpiPaymentInitiateResponse.builder()
                    .orderId(order.get("id"))
                    .upiLink(upiLink)
                    .qrCodeBase64(generateDummyQrBase64(upiLink)) // Optional or real QR generator
                    .status("CREATED")
                    .message("UPI payment initiated successfully.")
                    .build();

        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order: {}", e.getMessage(), e);

            return UpiPaymentInitiateResponse.builder()
                    .status("FAILED")
                    .message("Failed to initiate UPI payment: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Generates a base64 placeholder for the UPI QR code (you can replace this with
     * actual QR generation).
     */
    private String generateDummyQrBase64(String upiLink) {
        // This is just a placeholder – use ZXing or similar lib in real use case
        return Base64.getEncoder().encodeToString(("QR:" + upiLink).getBytes());
    }
}
