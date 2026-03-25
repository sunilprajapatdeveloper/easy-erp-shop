package nextpos.app.nextpos.ai.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.ai.dto.AiResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<AiResponse<?>> get(String key) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                AiResponse<?> response = objectMapper.readValue(json, AiResponse.class);
                log.info("Cache hit for key: {}", key);
                return Optional.of(response);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize cached response", e);
            }
        }
        return Optional.empty();
    }

    public void put(String key, AiResponse<?> response, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, json, ttl);
            log.debug("Cached response for key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize response for caching", e);
        }
    }
}