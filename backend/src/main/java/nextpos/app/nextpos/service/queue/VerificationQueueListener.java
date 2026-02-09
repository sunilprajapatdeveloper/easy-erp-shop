package nextpos.app.nextpos.service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.kafka.VerificationEmailEvent;
import nextpos.app.nextpos.service.email.VerificationEmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationQueueListener {

    private final VerificationEmailService verificationEmailService;

    @KafkaListener(topics = "${kafka.topics.verification-email}", groupId = "${kafka.consumer-groups.verification}", containerFactory = "verificationKafkaListenerContainerFactory")
    public void handleVerificationEmail(@Payload VerificationEmailEvent event, Acknowledgment ack) {
        try {
            log.info("Received verification email event for verificationId: {}, email: {}",
                    event.getVerificationId(), event.getEmail());

            verificationEmailService.sendVerificationEmail(
                    event.getVerificationId(),
                    event.getEmail(),
                    event.getToken(),
                    event.getVerificationType(),
                    event.getExpiryDuration());

            ack.acknowledge();
            log.info("Successfully processed verification email for verificationId: {}", event.getVerificationId());

        } catch (Exception e) {
            log.error("Failed to process verification email event for verificationId: {}, email: {}",
                    event.getVerificationId(), event.getEmail(), e);
            throw new RuntimeException("Failed to process verification email", e);
        }
    }
}