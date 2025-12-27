package nextpos.app.nextpos.service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.service.interf.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentQueueListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-processing-topic", groupId = "payment-group")
    public void listen(CreatePaymentRequest request) {
        log.info("Received payment request from Kafka: {}", request);

        try {
            paymentService.createPayment(request);
            log.info("Payment processed for referenceId: {}", request.getReferenceId());
        } catch (Exception e) {
            log.error("Error while processing payment: {}", e.getMessage(), e);
        }
    }
}
