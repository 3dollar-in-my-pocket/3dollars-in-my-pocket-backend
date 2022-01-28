package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.ValidationException
import com.depromeet.threedollar.common.exception.type.ErrorCode

object LocationValidator {

    fun validate(latitude: Double?, longitude: Double?) {
        if (latitude == null && longitude == null) {
            return
        }
        if ((latitude == null && longitude != null) || (latitude != null && longitude == null)) {
            throw ValidationException("위도(${latitude}) 경도(${longitude} 둘 중 하나만 null일 수 없습니다.")
        }
        validateLocation(latitude ?: DEFAULT, longitude ?: DEFAULT)
    }

    private fun validateLocation(latitude: Double, longitude: Double) {
        if (latitude < SOUTH_KOREA_MIN_LATITUDE || latitude > SOUTH_KOREA_MAX_LATITUDE || latitude == DEFAULT) {
            throw ValidationException("허용되지 않는 위도 ($latitude) 가 입력되었습니다.", ErrorCode.VALIDATION_LATITUDE_EXCEPTION)
        }
        if (longitude < SOUTH_KOREA_MIN_LONGITUDE || longitude > SOUTH_KOREA_MAX_LONGITUDE || longitude == DEFAULT) {
            throw ValidationException("허용되지 않는 경도 ($longitude) 가 입력되었습니다.", ErrorCode.VALIDATION_LONGITUDE_EXCEPTION)
        }
    }

    private const val DEFAULT = 0.0
    private const val SOUTH_KOREA_MIN_LATITUDE = 33.1
    private const val SOUTH_KOREA_MAX_LATITUDE = 38.61
    private const val SOUTH_KOREA_MIN_LONGITUDE = 124.60
    private const val SOUTH_KOREA_MAX_LONGITUDE = 131.87

}
