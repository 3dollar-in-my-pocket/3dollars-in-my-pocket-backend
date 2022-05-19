package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.common.domain.LocationValidator

/**
 * (longitude, latitude) 순서로 저장되어야 합니다.
 */
data class BossStoreLocation(
    val longitude: Double,
    val latitude: Double
) {

    init {
        LocationValidator.validate(latitude = latitude, longitude = longitude)
    }

    fun hasChangedLocation(latitude: Double, longitude: Double): Boolean {
        return !hasSameLocation(latitude = latitude, longitude = longitude)
    }

    private fun hasSameLocation(latitude: Double, longitude: Double): Boolean {
        return this.latitude == latitude && this.longitude == longitude
    }

    companion object {
        fun of(latitude: Double, longitude: Double): BossStoreLocation {
            return BossStoreLocation(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

}
