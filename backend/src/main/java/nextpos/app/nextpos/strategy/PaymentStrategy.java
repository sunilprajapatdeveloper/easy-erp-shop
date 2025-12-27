package nextpos.app.nextpos.strategy;

import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;

public interface PaymentStrategy {
    boolean supports(String paymentMethod); // e.g., CARD, CASH, etc.

    PaymentResponse pay(CreatePaymentRequest request);
}
