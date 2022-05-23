package com.depromeet.threedollar.domain.redis.domain.vendor.store

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

@Repository
class AroundVendorStoresCacheRepository(
    private val aroundStoresRedisRepository: StringRedisRepository<AroundVendorStoresCacheKey, List<VendorStoreCacheModel>>,
) {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<VendorStoreCacheModel>? {
        val key = AroundVendorStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<VendorStoreCacheModel>) {
        val key = AroundVendorStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
