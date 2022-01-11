package com.depromeet.threedollar.domain.common.domain;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.FORBIDDEN_LATITUDE_EXCEPTION;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.FORBIDDEN_LONGITUDE_EXCEPTION;

/**
 * 대한민국의 위도 / 경도
 * 위도: 북위 33.1 (제주특별자치도 서귀포시 대정읍의 마라도) ~ 북위 38.61 (강원도 고성군 현내면 대강리)
 * 경도: 동경 124.60 (인천광역시 옹진군 백령면의 백령도) ~ 131.87 (경상북도 울릉군 울릉읍의 독도)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Location {

    private static final double SOUTH_KOREA_MIN_LATITUDE = 33.1;
    private static final double SOUTH_KOREA_MAX_LATITUDE = 38.61;

    private static final double SOUTH_KOREA_MIN_LONGITUDE = 124.60;
    private static final double SOUTH_KOREA_MAX_LONGITUDE = 131.87;

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
        if (latitude < SOUTH_KOREA_MIN_LATITUDE || latitude > SOUTH_KOREA_MAX_LATITUDE) {
            throw new ValidationException(String.format("잘못된 위도 (%s)가 입력되었습니다. (%s ~ %s) 사이의 위도만 허용됩니다)", latitude, SOUTH_KOREA_MIN_LATITUDE, SOUTH_KOREA_MAX_LATITUDE), FORBIDDEN_LATITUDE_EXCEPTION);
        }
        if (longitude < SOUTH_KOREA_MIN_LONGITUDE || longitude > SOUTH_KOREA_MAX_LONGITUDE) {
            throw new ValidationException(String.format("잘못된 경도 (%s)가 입력되었습니다. (%s ~ %s) 사이의 경도만 허용됩니다)", longitude, SOUTH_KOREA_MIN_LONGITUDE, SOUTH_KOREA_MAX_LONGITUDE), FORBIDDEN_LONGITUDE_EXCEPTION);
        }
    }

    public static Location of(double latitude, double longitude) {
        return new Location(latitude, longitude);
    }

}
