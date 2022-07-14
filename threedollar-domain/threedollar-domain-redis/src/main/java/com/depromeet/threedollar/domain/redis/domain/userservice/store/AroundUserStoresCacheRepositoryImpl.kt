package com.depromeet.threedollar.domain.redis.domain.userservice.store

import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

@Repository
class AroundUserStoresCacheRepositoryImpl(
    private val aroundStoresRedisRepository: StringRedisRepository<AroundUserStoresCacheKey, List<UserStoreCacheModel>>,
) : AroundUserStoresCacheRepository {

    override fun getCache(mapLatitude: Double, mapLongitude: Double, distance: Double): List<UserStoreCacheModel>? {
        val key = AroundUserStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        return aroundStoresRedisRepository.get(key)
    }

    override fun setCache(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<UserStoreCacheModel>) {
        val key = AroundUserStoresCacheKey(
            mapLatitude = mapLatitude,
            mapLongitude = mapLongitude,
            distance = distance
        )
        aroundStoresRedisRepository.set(key, value)
    }

}
