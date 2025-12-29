package nextpos.app.nextpos.service.queue;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.EmailRequest;
import nextpos.app.nextpos.service.email.MailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailQueueListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MailService mailService;

    private static final String EMAIL_QUEUE = "emailQueue";

    @PostConstruct
    public void startListening() {
        Thread thread = new Thread(() -> {
            log.info("Email Queue Listener thread started.");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Attempt to pop from Redis
                    EmailRequest request = (EmailRequest) redisTemplate.opsForList().leftPop(EMAIL_QUEUE);

                    if (request != null) {
                        try {
                            if (request.isHtml()) {
                                mailService.sendHtmlEmail(request.getTo(), request.getSubject(), request.getContent());
                            } else {
                                mailService.sendPlainTextEmail(request.getTo(), request.getSubject(),
                                        request.getContent());
                            }
                            log.info("Successfully sent email to {}", request.getTo());
                        } catch (Exception e) {
                            log.error("Failed to send email to {}: {}", request.getTo(), e.getMessage());
                        }
                    }

                    // Standard sleep to prevent high CPU usage when queue is empty
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    log.warn("Email Queue Listener interrupted, shutting down...");
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    // This catches RedisConnectionFailureException and prevents the thread from
                    // dying
                    log.error("Redis connection error in EmailQueueListener: {}. Retrying in 5 seconds...",
                            e.getMessage());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("Email-Queue-Thread");
        thread.start();
    }
}