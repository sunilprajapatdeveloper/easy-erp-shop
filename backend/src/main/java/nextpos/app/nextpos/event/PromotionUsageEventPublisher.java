package nextpos.app.nextpos.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionUsageEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "promotion-usage-topic";

    public void publish(PromotionUsageEvent event) {
        log.info("Publishing promotion usage event: {}", event);
        kafkaTemplate.send(TOPIC, event);
    }
}