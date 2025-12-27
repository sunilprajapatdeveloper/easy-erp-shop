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
            while (true) {
                EmailRequest request = (EmailRequest) redisTemplate.opsForList().leftPop(EMAIL_QUEUE);
                if (request != null) {
                    try {
                        if (request.isHtml()) {
                            mailService.sendHtmlEmail(request.getTo(), request.getSubject(), request.getContent());
                        } else {
                            mailService.sendPlainTextEmail(request.getTo(), request.getSubject(), request.getContent());
                        }
                        log.info("Sent email to {}", request.getTo());
                    } catch (Exception e) {
                        log.error("Failed to send email to {}", request.getTo(), e);
                    }
                }
                try {
                    Thread.sleep(500); // Prevent tight loop when empty
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}