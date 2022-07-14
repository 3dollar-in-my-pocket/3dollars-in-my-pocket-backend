package com.depromeet.threedollar.domain.redis.domain.userservice.store

interface AroundUserStoresCacheRepository {

    fun getCache(mapLatitude: Double, mapLongitude: Double, distance: Double): List<UserStoreCacheModel>?

    fun setCache(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<UserStoreCacheModel>)

}
