package com.depromeet.threedollar.domain.redis.core;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

public interface StringRedisRepository<K, V> {

    @Nullable
    V get(K key);

    List<V> getBulk(List<K> keys);

    void set(K key, V value);

    void setTtl(K key, V value, Duration ttl);

    void increase(K key);

    void increaseBulk(List<K> keys);

    void increase(K key, long value);

    void decrease(K key);

    void decrease(K key, long value);

    void delete(K key);

}
