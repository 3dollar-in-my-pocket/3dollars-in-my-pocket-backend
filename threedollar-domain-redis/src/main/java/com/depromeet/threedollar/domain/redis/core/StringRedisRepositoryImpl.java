package com.depromeet.threedollar.domain.redis.core;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class StringRedisRepositoryImpl<KEY extends StringRedisKey<VALUE>, VALUE> implements StringRedisRepository<KEY, VALUE> {

    private final RedisTemplate<String, String> redisTemplate;

    @Nullable
    @Override
    public VALUE get(KEY key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        key.getValue(operations.get(key.getKey()));
        return key.getValue(operations.get(key.getKey()));
    }

    @Nullable
    @Override
    public List<VALUE> multiGet(List<KEY> keys) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(String.format("키는 최소 한개 이상 입력해주세요. keys: (%s)", keys));
        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        List<String> values = operations.multiGet(keys.stream().map(KEY::getKey).collect(Collectors.toList()));
        if (values == null) {
            return null;
        }
        KEY key = keys.get(0);

        return values.stream()
            .map(key::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public void set(KEY key, VALUE value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if (key.getTtl() == null) {
            operations.set(key.getKey(), key.toValue(value));
            return;
        }
        operations.set(key.getKey(), key.toValue(value), key.getTtl().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void incr(KEY key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey());
    }

    @Override
    public void incr(KEY key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.increment(key.getKey(), value);
    }

    @Override
    public void decr(KEY key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey());
    }

    @Override
    public void decr(KEY key, long value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.decrement(key.getKey(), value);
    }

    @Override
    public void delete(KEY key) {
        redisTemplate.delete(key.getKey());
    }

}
