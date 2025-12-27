package nextpos.app.nextpos.service.queue;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.EmailRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailQueuePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMAIL_QUEUE = "emailQueue";

    public void publishEmail(EmailRequest request) {
        redisTemplate.opsForList().rightPush(EMAIL_QUEUE, request);
    }
}
