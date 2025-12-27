package nextpos.app.nextpos.messaging.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.response.MediaResponse;

import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String MEDIA_UPLOAD_TOPIC = "media.upload";
    private static final String MEDIA_DELETE_TOPIC = "media.delete";

    public void sendMediaUploadEvent(MediaResponse mediaResponse) {
        try {
            kafkaTemplate.send(MEDIA_UPLOAD_TOPIC, mediaResponse.getId(), mediaResponse);
            log.info("Media upload event sent for: {}", mediaResponse.getId());
        } catch (Exception e) {
            log.error("Failed to send media upload event", e);
        }
    }

    public void sendMediaDeleteEvent(String mediaId) {
        try {
            kafkaTemplate.send(MEDIA_DELETE_TOPIC, mediaId, Map.of(
                    "mediaId", mediaId,
                    "timestamp", System.currentTimeMillis()));
            log.info("Media delete event sent for: {}", mediaId);
        } catch (Exception e) {
            log.error("Failed to send media delete event", e);
        }
    }
}