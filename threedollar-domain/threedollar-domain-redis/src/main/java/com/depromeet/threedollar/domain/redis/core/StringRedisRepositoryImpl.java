package com.depromeet.threedollar.domain.redis.core;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

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
            return new ArrayList<>();
        }
        K key = keys.get(0);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        List<List<K>> partitionedKeys = PartitionUtils.partition(keys, FETCH_SIZE);
        List<String> values = partitionedKeys.stream()
            .map(keyList -> operations.multiGet(keyList.stream()
                .map(K::getKey).
                collect(Collectors.toList())))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        return values.stream()
            .map(key::deserializeValue)
            .collect(Collectors.toList());
    }

    @Override
    public void set(@NotNull K key, @NotNull V value) {
        setWithTtl(key, value, key.getTtl());
    }

    @Override
    public void setBulk(Map<K, V> keyValues) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        redisTemplate.executePipelined((RedisCallback<Object>) pipeline -> {
            keyValues.forEach((key, value) -> operations.set(key.getKey(), String.valueOf(value)));
            return null;
        });
    }

    @Override
    public void setWithTtl(@NotNull K key, @NotNull V value, @Nullable Duration ttl) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if (ttl == null) {
            operations.set(key.getKey(), key.serializeValue(value));
            return;
        }
        operations.set(key.getKey(), key.serializeValue(value), ttl.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void incr(@NotNull K key) {
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
    public void incrBy(@NotNull K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey(), value);
    }

    @Override
    public void decr(@NotNull K key) {
        decrBy(key, 1);
    }

    @Override
    public void decrBulk(List<K> keys) {
        redisTemplate.executePipelined((RedisCallback<Object>) pipeline -> {
            keys.forEach(key -> pipeline.decr(key.getKey().getBytes(StandardCharsets.UTF_8)));
            return null;
        });
    }

    @Override
    public void decrBy(@NotNull K key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey(), value);
    }

    @Override
    public void del(@NotNull K key) {
        redisTemplate.delete(key.getKey());
    }

    @Override
    public void delBulk(List<K> keys) {
        for (List<K> partitionedKeys : PartitionUtils.partition(keys, FETCH_SIZE)) {
            Set<String> keyStrings = partitionedKeys.stream()
                .map(K::getKey)
                .collect(Collectors.toSet());
            redisTemplate.delete(keyStrings);
        }
    }

}
