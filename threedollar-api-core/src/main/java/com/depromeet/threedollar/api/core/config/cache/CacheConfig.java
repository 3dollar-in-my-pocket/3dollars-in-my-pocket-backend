package com.depromeet.threedollar.api.core.config.cache;

import com.depromeet.threedollar.common.type.CacheType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("!integration-test")
@RequiredArgsConstructor
@EnableCaching
@Configuration
public class CacheConfig {

    private static final Duration DEFAULT_CACHE_DURATION = Duration.ofMinutes(30);

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager compositeCacheManager() {
        return new CompositeCacheManager(caffeineCacheManager(), redisCacheManager());
    }

    private CacheManager caffeineCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caffeineCaches());
        return simpleCacheManager;
    }

    private List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values())
            .filter(CacheType::isLocalCache)
            .map(cache -> new CaffeineCache(cache.getKey(),
                    Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(cache.getDuration())
                        .build()
                )
            ).collect(Collectors.toList());
    }

    private CacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultRedisCacheConfiguration())
            .withInitialCacheConfigurations(customRedisCacheConfiguration())
            .build();
    }

    private RedisCacheConfiguration defaultRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
            .entryTtl(DEFAULT_CACHE_DURATION);
    }

    private Map<String, RedisCacheConfiguration> customRedisCacheConfiguration() {
        return Arrays.stream(CacheType.values())
            .filter(CacheType::isGlobalCache)
            .collect(Collectors.toMap(CacheType::getKey, cacheType -> RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                .entryTtl(cacheType.getDuration())));
    }

}
