package com.depromeet.threedollar.domain.redis.domain.user.store

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

@Repository
class AroundUserStoresCacheRepository(
    private val aroundStoresRedisRepository: StringRedisRepository<AroundUserStoresCacheKey, List<UserStoreCacheModel>>,
) {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<UserStoreCacheModel>? {
        val key = AroundUserStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<UserStoreCacheModel>) {
        val key = AroundUserStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
