package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode

/**
 * (longitude, latitude) 순서로 저장되어야 합니다.
 */

private const val SOUTH_KOREA_MIN_LATITUDE = 33.1
private const val SOUTH_KOREA_MAX_LATITUDE = 38.61
private const val SOUTH_KOREA_MIN_LONGITUDE = 124.60
private const val SOUTH_KOREA_MAX_LONGITUDE = 131.87

data class BossStoreLocation(
    val longitude: Double,
    val latitude: Double
) {

    init {
        if (latitude < SOUTH_KOREA_MIN_LATITUDE || latitude > SOUTH_KOREA_MAX_LATITUDE) {
            throw InvalidException("허용되지 않는 위도 ($latitude) 가 입력되었습니다.", ErrorCode.INVALID_MISSING_LATITUDE)
        }
        if (longitude < SOUTH_KOREA_MIN_LONGITUDE || longitude > SOUTH_KOREA_MAX_LONGITUDE) {
            throw InvalidException("허용되지 않는 경도 ($longitude) 가 입력되었습니다.", ErrorCode.INVALID_MISSING_LONGITUDE)
        }
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
