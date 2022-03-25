package com.depromeet.threedollar.domain.redis.core;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StringRedisRepository<K, V> {

    @Nullable
    V get(K key);

    List<V> multiGet(List<K> keys);

    void set(K key, V value);

    void incr(K key);

    void multiIncr(List<K> keys);

    void incr(K key, long value);

    void decr(K key);

    void decr(K key, long value);

    void delete(K key);

}
