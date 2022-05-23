package com.depromeet.threedollar.domain.redis.domain.vendor.store

import java.time.Duration
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.common.utils.decode
import com.depromeet.threedollar.domain.redis.core.StringRedisKey

data class AroundVendorStoresCacheKey(
    private val mapLatitude: Double,
    private val mapLongitude: Double,
    private val distance: Double,
) : StringRedisKey<List<VendorStoreCacheModel>> {

    override fun getKey(): String {
        return "user:v1:stores:around:latitude:$mapLatitude:longitude:$mapLatitude:distance:$distance"
    }

    override fun deserializeValue(value: String?): List<VendorStoreCacheModel>? {
        return value?.let { JsonUtils.getObjectMapper().decode(value) }
    }

    override fun serializeValue(value: List<VendorStoreCacheModel>): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return Duration.ofMinutes(1)
    }

}
