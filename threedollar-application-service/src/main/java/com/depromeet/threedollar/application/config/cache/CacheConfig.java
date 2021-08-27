package com.depromeet.threedollar.application.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableCaching
@Configuration
public class CacheConfig {

    private static final Duration DEFAULT_CACHE_DURATION = Duration.ofHours(1);

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
            .entryTtl(DEFAULT_CACHE_DURATION);
    }

    @Bean
    public Map<String, RedisCacheConfiguration> customCacheConfiguration() {
        Map<String, RedisCacheConfiguration> customCachePolicies = new HashMap<>();
        for (CacheConstants constants : CacheConstants.values()) {
            customCachePolicies.put(constants.getKey(), RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(constants.getDuration()));
        }
        return customCachePolicies;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfiguration())
            .withInitialCacheConfigurations(customCacheConfiguration())
            .build();
    }

}
