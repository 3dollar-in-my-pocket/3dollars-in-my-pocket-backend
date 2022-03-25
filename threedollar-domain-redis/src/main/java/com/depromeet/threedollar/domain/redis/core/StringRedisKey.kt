package com.depromeet.threedollar.domain.redis.core

import java.time.Duration

interface StringRedisKey<V> {

    fun getKey(): String

    fun getValue(value: String?): V?

    fun toValue(value: V): String

    fun getTtl(): Duration?

}
