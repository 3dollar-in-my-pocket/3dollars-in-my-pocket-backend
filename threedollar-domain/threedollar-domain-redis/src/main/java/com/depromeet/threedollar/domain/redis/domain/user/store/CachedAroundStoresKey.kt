package com.depromeet.threedollar.domain.redis.domain.user.store

import java.time.Duration
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.common.utils.decode
import com.depromeet.threedollar.domain.redis.core.StringRedisKey
import com.depromeet.threedollar.domain.redis.domain.user.store.dto.CachedUserStoreDto

data class CachedAroundStoresKey(
    private val mapLatitude: Double,
    private val mapLongitude: Double,
    private val distance: Double,
) : StringRedisKey<List<CachedUserStoreDto>> {

    override fun getKey(): String {
        return "user:v1:stores:around:latitude:$mapLatitude:longitude:$mapLatitude:distance:$distance"
    }

    override fun deserializeValue(value: String?): List<CachedUserStoreDto>? {
        return value?.let { JsonUtils.getObjectMapper().decode(value) }
    }

    override fun serializeValue(value: List<CachedUserStoreDto>): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return Duration.ofMinutes(1)
    }

}