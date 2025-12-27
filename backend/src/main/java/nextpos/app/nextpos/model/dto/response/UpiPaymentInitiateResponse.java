package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpiPaymentInitiateResponse {

    private String orderId; // Razorpay order ID
    private String upiLink; // UPI payment deep link or QR string
    private String qrCodeBase64; // Optional: QR code image as base64 string
    private String status; // CREATED / FAILED
    private String message; // Success or error message
}
