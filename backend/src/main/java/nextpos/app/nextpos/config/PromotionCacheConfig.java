package nextpos.app.nextpos.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class PromotionCacheConfig {

        @Bean(name = "promotionCacheManager")
        public CacheManager promotionCacheManager(RedisConnectionFactory redisConnectionFactory) {

                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                                .disableCachingNullValues()
                                .entryTtl(Duration.ofMinutes(10));

                return RedisCacheManager.builder(redisConnectionFactory)
                                .cacheDefaults(defaultConfig)
                                .withCacheConfiguration("activePromotions",
                                                defaultConfig.entryTtl(Duration.ofMinutes(5)))
                                .withCacheConfiguration("promotionValidation",
                                                defaultConfig.entryTtl(Duration.ofMinutes(2)))
                                .build();
        }
}