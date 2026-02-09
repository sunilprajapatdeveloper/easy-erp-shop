package nextpos.app.nextpos.service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.kafka.VerificationEmailEvent;
import nextpos.app.nextpos.model.enums.VerificationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationQueuePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.verification-email}")
    private String topicName;

    public void publishVerificationEmail(UUID verificationId, String email,
            String token, VerificationType type, Duration expiryDuration) {

        VerificationEmailEvent event = VerificationEmailEvent.builder()
                .verificationId(verificationId)
                .email(email)
                .token(token)
                .verificationType(type)
                .expiryDuration(expiryDuration)
                .build();

        try {
            kafkaTemplate.send(topicName, verificationId.toString(), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Verification email queued for {} with id: {}, offset: {}",
                                    email, verificationId, result.getRecordMetadata().offset());
                        } else {
                            log.error("Failed to queue verification email for {}: {}", email, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Error publishing to Kafka for email {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to queue verification email", e);
        }
    }
}