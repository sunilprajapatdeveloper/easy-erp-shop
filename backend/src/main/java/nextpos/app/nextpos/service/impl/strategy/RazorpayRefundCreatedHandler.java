package nextpos.app.nextpos.service.impl.strategy;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRazorpayWebhookRequest;
import org.springframework.stereotype.Component;
import nextpos.app.nextpos.service.interf.RazorpayWebhookEventHandler;

@Slf4j
@Component("refund.created")
public class RazorpayRefundCreatedHandler implements RazorpayWebhookEventHandler {

    @Override
    public void handle(CreateRazorpayWebhookRequest request) {
        log.info("Handling refund created webhook: {}", request);
        // TODO: implement your logic here (reverse order, notify accounting, etc.)
    }
}