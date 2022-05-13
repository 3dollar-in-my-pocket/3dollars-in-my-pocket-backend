package com.depromeet.threedollar.domain.redis.domain.user.store

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

@Repository
class CachedAroundStoreRepository(
    private val aroundStoresRedisRepository: StringRedisRepository<CachedAroundStoreKey, List<CachedAroundStoreValue>>
) {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<CachedAroundStoreValue>? {
        val key = CachedAroundStoreKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<CachedAroundStoreValue>) {
        val key = CachedAroundStoreKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
