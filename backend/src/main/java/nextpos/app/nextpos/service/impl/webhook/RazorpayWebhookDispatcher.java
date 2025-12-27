package nextpos.app.nextpos.service.impl.webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRazorpayWebhookRequest;
import nextpos.app.nextpos.service.interf.RazorpayWebhookEventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorpayWebhookDispatcher {

    private final Map<String, RazorpayWebhookEventHandler> handlerMap = new HashMap<>();

    // Inject all handlers using constructor injection
    public RazorpayWebhookDispatcher(
            Map<String, RazorpayWebhookEventHandler> strategyMap) {
        this.handlerMap.putAll(strategyMap);
    }

    /**
     * Dispatch the webhook event to the appropriate handler based on event name.
     *
     * @param request full Razorpay webhook request
     */
    public void dispatch(CreateRazorpayWebhookRequest request) {
        String event = request.getEvent();
        RazorpayWebhookEventHandler handler = handlerMap.get(event);

        if (handler != null) {
            log.info("Dispatching Razorpay event: {}", event);
            handler.handle(request);
        } else {
            log.warn("No handler found for Razorpay event: {}", event);
        }
    }
}
