package com.depromeet.threedollar.domain.redis.core;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class StringRedisRepositoryImpl<K extends StringRedisKey<V>, V> implements StringRedisRepository<K, V> {

    private final RedisTemplate<String, String> redisTemplate;

    @Nullable
    @Override
    public V get(K key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return key.deserializeValue(operations.get(key.getKey()));
    }

    @Override
    public List<V> getBulk(List<K> keys) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(String.format("Redis 벌크 조회시 keys(%s)가 비어있을 수 없습니다", keys));
        }

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        List<String> values = operations.multiGet(keys.stream()
            .map(K::getKey)
            .collect(Collectors.toList()));

        if (values == null) {
            throw new InternalServerException(String.format("Redis multiGet 중 에러가 발생하였습니다. values가 널입니다. keys: (%s)", keys));
        }

        K key = keys.get(0);
        return values.stream()
            .map(key::deserializeValue)
            .collect(Collectors.toList());
    }

    @Override
    public void set(K key, V value) {
        setWithTtl(key, value, key.getTtl());
    }

    @Override
    public void setWithTtl(K key, V value, Duration ttl) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Duration duration = key.getTtl();
        if (duration == null) {
            operations.set(key.getKey(), key.serializeValue(value));
            return;
        }
        operations.set(key.getKey(), key.serializeValue(value), duration.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void incr(K key) {
        incrBy(key, 1);
    }

    @Override
    public void incrBulk(List<K> keys) {
        redisTemplate.executePipelined((RedisCallback<Object>) pipeline -> {
            keys.forEach(key -> pipeline.incr(key.getKey().getBytes(StandardCharsets.UTF_8)));
            return null;
        });
    }

    @Override
    public void incrBy(K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey(), value);
    }

    @Override
    public void decr(K key) {
        decrBy(key, 1);
    }

    @Override
    public void decrBy(K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey(), value);
    }

    @Override
    public void del(K key) {
        redisTemplate.delete(key.getKey());
    }

}
