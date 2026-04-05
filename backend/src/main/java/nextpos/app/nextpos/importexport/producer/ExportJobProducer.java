package nextpos.app.nextpos.importexport.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJobProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "export-job";

    public void send(Map<String, Object> message) {
        kafkaTemplate.send(TOPIC, message);
        log.info("Sent export job message: {}", message);
    }
}