package nextpos.app.nextpos.controller.webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.RazorpayConfig;
import nextpos.app.nextpos.model.dto.request.CreateRazorpayWebhookRequest;
import nextpos.app.nextpos.service.impl.webhook.RazorpayWebhookDispatcher;
import nextpos.app.nextpos.util.RazorpaySignatureValidator;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/webhooks/razorpay")
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebhookController {

    private final RazorpayWebhookDispatcher dispatcher;
    private final RazorpaySignatureValidator signatureValidator;
    private final RazorpayConfig razorpayConfig;

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
            @RequestHeader("X-Razorpay-Signature") String razorpaySignature) {
        if (!razorpayConfig.isEnableWebhook()) {
            log.warn("Razorpay webhook handling is disabled via config");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Webhook disabled");
        }

        try {
            String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);

            if (!signatureValidator.isValidSignature(payload, razorpaySignature)) {
                log.warn("Invalid Razorpay signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            CreateRazorpayWebhookRequest webhookRequest = CreateRazorpayWebhookRequest.fromJson(
                    payload,
                    razorpaySignature,
                    request.getRemoteAddr());
            dispatcher.dispatch(webhookRequest);

            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            log.error("Error processing Razorpay webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook processing failed");
        }
    }
}
