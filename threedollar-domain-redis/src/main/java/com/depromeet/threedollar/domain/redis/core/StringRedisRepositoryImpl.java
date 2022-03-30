package com.depromeet.threedollar.domain.redis.core;

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
        key.getValue(operations.get(key.getKey()));
        return key.getValue(operations.get(key.getKey()));
    }

    @Override
    public List<V> getBulk(List<K> keys) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        List<String> values = operations.multiGet(keys.stream()
            .map(K::getKey)
            .collect(Collectors.toList()));

        if (values == null) {
            throw new IllegalArgumentException(String.format("레디스 multiGet (%s) 중 에러가 발생하였습니다", keys));
        }

        K key = keys.get(0);
        return values.stream()
            .map(key::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public void set(K key, V value) {
        if (key.getTtl() == null) {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key.getKey(), key.toValue(value));
            return;
        }
        setTtl(key, value, key.getTtl());
    }

    @Override
    public void setTtl(K key, V value, Duration ttl) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key.getKey(), key.toValue(value), key.getTtl().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void increase(K key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey());
    }

    @Override
    public void increaseBulk(List<K> keys) {
        redisTemplate.executePipelined((RedisCallback<Object>) pipeline -> {
            keys.forEach(key -> pipeline.incr(key.getKey().getBytes(StandardCharsets.UTF_8)));
            return null;
        });
    }

    @Override
    public void increase(K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey(), value);
    }

    @Override
    public void decrease(K key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey());
    }

    @Override
    public void decrease(K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey(), value);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key.getKey());
    }

}
