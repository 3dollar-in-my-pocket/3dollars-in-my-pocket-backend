package com.depromeet.threedollar.domain.redis.domain.user.store

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import com.depromeet.threedollar.domain.redis.domain.user.store.dto.CachedUserStoreDto

@Repository
class CachedAroundStoresRepository(
    private val aroundStoresRedisRepository: StringRedisRepository<CachedAroundStoresKey, List<CachedUserStoreDto>>,
) {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<CachedUserStoreDto>? {
        val key = CachedAroundStoresKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<CachedUserStoreDto>) {
        val key = CachedAroundStoresKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
