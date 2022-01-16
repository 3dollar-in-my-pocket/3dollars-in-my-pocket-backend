package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.ValidationException

object LocationValidator {

    fun validate(latitude: Double, longitude: Double) {
        if (latitude < SOUTH_KOREA_MIN_LATITUDE || latitude > SOUTH_KOREA_MAX_LATITUDE) {
            throw ValidationException("허용되지 않는 위도 ($latitude) 가 입력되었습니다.")
        }
        if (longitude < SOUTH_KOREA_MIN_LONGITUDE || longitude > SOUTH_KOREA_MAX_LONGITUDE) {
            throw ValidationException("허용되지 않는 경도 ($longitude) 가 입력되었습니다.")
        }
    }

    private const val SOUTH_KOREA_MIN_LATITUDE = 33.1
    private const val SOUTH_KOREA_MAX_LATITUDE = 38.61
    private const val SOUTH_KOREA_MIN_LONGITUDE = 124.60
    private const val SOUTH_KOREA_MAX_LONGITUDE = 131.87

}