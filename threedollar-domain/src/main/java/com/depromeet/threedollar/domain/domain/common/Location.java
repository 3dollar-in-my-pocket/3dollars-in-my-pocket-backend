package com.depromeet.threedollar.domain.domain.common;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_LATITUDE_EXCEPTION;
import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_LONGITUDE_EXCEPTION;

/**
 * 대한민국의 위도 / 경도
 * 한반도 전체
 * 위도: 북위 33.11111111 ~ 북위 43.00972222
 * 경도: 동경 124.19583333 ~ 동경 131.87222222
 * -
 * 북한 제외
 * 위도: 북위 33.10000000 ~ 북위 38.45000000
 * 경도: 동경 125.06666667 ~ 131.87222222
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Location {

    private static final double KOREA_MIN_LATITUDE = 33.10000000;
    private static final double KOREA_MAX_LATITUDE = 38.45000000;

    private static final double KOREA_MIN_LONGITUDE = 125.06666667;
    private static final double KOREA_MAX_LONGITUDE = 131.87222222;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private Location(double latitude, double longitude) {
        validateIsScopeOfKorea(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void validateIsScopeOfKorea(double latitude, double longitude) {
        if (latitude < KOREA_MIN_LATITUDE || latitude > KOREA_MAX_LATITUDE) {
            throw new ValidationException(String.format("잘못된 위도 (%s)가 입력되었습니다. (33 ~ 43) 사이의 범위만 허용됩니다)", latitude), VALIDATION_LATITUDE_EXCEPTION);
        }
        if (longitude < KOREA_MIN_LONGITUDE || longitude > KOREA_MAX_LONGITUDE) {
            throw new ValidationException(String.format("잘못된 경도 (%s)가 입력되었습니다. (124 ~ 132) 사이의 범위만 허용됩니다)", longitude), VALIDATION_LONGITUDE_EXCEPTION);
        }
    }

    public static Location of(double latitude, double longitude) {
        return new Location(latitude, longitude);
    }

}
