package com.depromeet.threedollar.domain.redis.domain.user.store

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import com.depromeet.threedollar.domain.redis.domain.user.store.dto.UserStoreRedisDto

@Repository
class CachedAroundStoreRepository(
    private val aroundStoresRedisRepository: StringRedisRepository<CachedAroundStoreKey, List<UserStoreRedisDto>>
) {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<UserStoreRedisDto>? {
        val key = CachedAroundStoreKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<UserStoreRedisDto>) {
        val key = CachedAroundStoreKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
