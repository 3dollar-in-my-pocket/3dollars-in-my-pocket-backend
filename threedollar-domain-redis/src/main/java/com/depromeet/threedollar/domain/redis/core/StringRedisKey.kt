package com.depromeet.threedollar.domain.redis.core

import java.time.Duration

interface StringRedisKey<VALUE> {

    fun getKey(): String

    fun getValue(value: String?): VALUE?

    fun toValue(value: VALUE): String

    fun getTtl(): Duration?

}
