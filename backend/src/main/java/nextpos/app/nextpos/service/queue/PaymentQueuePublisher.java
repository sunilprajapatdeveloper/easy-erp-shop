package nextpos.app.nextpos.service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentQueuePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PAYMENT_TOPIC = "payment-processing-topic";

    public void publish(CreatePaymentRequest request) {
        log.info("Publishing payment request to Kafka: {}", request);
        kafkaTemplate.send(PAYMENT_TOPIC, request);
    }
}
