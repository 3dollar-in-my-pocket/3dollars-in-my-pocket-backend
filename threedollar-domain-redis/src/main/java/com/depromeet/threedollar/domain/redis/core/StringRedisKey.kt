package com.depromeet.threedollar.domain.redis.core

import java.time.Duration

interface StringRedisKey<V> {

    fun getKey(): String

    fun deserializeValue(value: String?): V?

    fun serializeValue(value: V): String

    fun getTtl(): Duration?

}
