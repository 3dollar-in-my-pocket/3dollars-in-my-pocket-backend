package com.depromeet.threedollar.domain.redis.core;

import java.time.Duration;
import java.util.List;

import org.jetbrains.annotations.Nullable;

public interface StringRedisRepository<K, V> {

    @Nullable
    V get(K key);

    List<V> getBulk(List<K> keys);

    void set(K key, V value);

    void setWithTtl(K key, V value, Duration ttl);

    void incr(K key);

    void incrBulk(List<K> keys);

    void incrBy(K key, long value);

    void decr(K key);

    void decrBy(K key, long value);

    void del(K key);

}
