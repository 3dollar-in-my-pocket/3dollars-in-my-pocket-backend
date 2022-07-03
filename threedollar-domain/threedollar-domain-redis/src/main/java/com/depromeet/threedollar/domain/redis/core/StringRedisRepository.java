package com.depromeet.threedollar.domain.redis.core;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StringRedisRepository<K extends StringRedisKey<V>, V> {

    @Nullable
    V get(K key);

    List<V> getBulk(List<K> keys);

    void set(@NotNull K key, @NotNull V value);

    void setBulk(Map<K, V> keyValues);

    void setWithTtl(@NotNull K key, @NotNull V value, @NotNull Duration ttl);

    void incr(@NotNull K key);

    void incrBulk(List<K> keys);

    void incrBy(@NotNull K key, long value);

    void decr(@NotNull K key);

    void decrBulk(List<K> keys);

    void decrBy(@NotNull K key, long value);

    void del(@NotNull K key);

    void delBulk(List<K> keys);

}
