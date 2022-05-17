package com.depromeet.threedollar.common.utils.distance;

import com.depromeet.threedollar.common.exception.model.InvalidException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LookupRadiusDistanceLimiter {

    private static final double MAX_RADIUS_DISTANCE_KM = 2.0;

    public static double fromMtoKm(double distanceMeter) {
        return fromKmToKm(DistanceUnitConvertUtils.fromMeterToKm(distanceMeter));
    }

    public static double fromKmToKm(double distanceKiloMeter) {
        if (distanceKiloMeter < 0) {
            throw new InvalidException(String.format("거리(%s)km가 0보다 작을 수 없습니다", distanceKiloMeter));
        }
        return Math.min(distanceKiloMeter, MAX_RADIUS_DISTANCE_KM);
    }

}
