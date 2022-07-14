package com.depromeet.threedollar.domain.redis.domain.userservice.store

interface AroundUserStoresCacheRepository {

    fun get(mapLatitude: Double, mapLongitude: Double, distance: Double): List<UserStoreCacheModel>?

    fun set(mapLatitude: Double, mapLongitude: Double, distance: Double, value: List<UserStoreCacheModel>)

}
