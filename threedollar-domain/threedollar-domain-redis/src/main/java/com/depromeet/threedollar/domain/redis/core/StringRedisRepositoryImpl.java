package com.depromeet.threedollar.domain.redis.core;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.utils.PartitionUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StringRedisRepositoryImpl<K extends StringRedisKey<V>, V> implements StringRedisRepository<K, V> {

    private static final int FETCH_SIZE = 1000;

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
            throw new InternalServerException(String.format("Redis 벌크 조회시 keys(%s)가 비어있을 수 없습니다", keys));
        }

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        List<List<K>> partitionedKeys = PartitionUtils.partition(keys, FETCH_SIZE);
        List<String> values = partitionedKeys.stream()
            .map(keyList -> operations.multiGet(keyList.stream()
                .map(K::getKey).
                collect(Collectors.toList())))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

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
