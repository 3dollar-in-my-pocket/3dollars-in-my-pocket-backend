package com.depromeet.threedollar.domain.redis.core;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StringRedisRepository<KEY, VALUE> {

    @Nullable
    VALUE get(KEY key);

    List<VALUE> multiGet(List<KEY> keys);

    void set(KEY key, VALUE value);

    void incr(KEY key);

    void multiIncr(List<KEY> keys);

    void incr(KEY key, long value);

    void decr(KEY key);

    void decr(KEY key, long value);

    void delete(KEY key);

}
