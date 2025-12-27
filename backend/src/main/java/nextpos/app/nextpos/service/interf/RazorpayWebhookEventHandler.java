package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRazorpayWebhookRequest;

public interface RazorpayWebhookEventHandler {
    /**
     * Handle a specific Razorpay webhook event.
     *
     * @param request The full webhook payload received from Razorpay
     */
    void handle(CreateRazorpayWebhookRequest request);
}
