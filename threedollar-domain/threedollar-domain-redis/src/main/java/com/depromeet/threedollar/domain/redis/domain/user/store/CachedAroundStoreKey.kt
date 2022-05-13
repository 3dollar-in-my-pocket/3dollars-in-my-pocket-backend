package com.depromeet.threedollar.domain.redis.domain.user.store

import java.time.Duration
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.common.utils.decode
import com.depromeet.threedollar.domain.redis.core.StringRedisKey

data class CachedAroundStoreKey(
    private val mapLatitude: Double,
    private val mapLongitude: Double,
    private val distance: Double,
) : StringRedisKey<List<CachedAroundStoreValue>> {

    override fun getKey(): String {
        return "user:store:around:latitude:$mapLatitude:longitude:$mapLatitude:distance:$distance"
    }

    override fun deserializeValue(value: String?): List<CachedAroundStoreValue>? {
        return value?.let { JsonUtils.getObjectMapper().decode(value) }
    }

    override fun serializeValue(value: List<CachedAroundStoreValue>): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return Duration.ofMinutes(1)
    }

}
