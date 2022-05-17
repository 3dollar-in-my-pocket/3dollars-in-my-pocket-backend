package com.depromeet.threedollar.common.utils.distance;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LookupRadiusDistanceLimiter {

    private static final double MAX_RADIUS_DISTANCE_KM = 2.0;

    public static double fromMtoKm(double distanceMeter) {
        return Math.min(DistanceUnitConvertUtils.fromMeterToKm(distanceMeter), MAX_RADIUS_DISTANCE_KM);
    }

    public static double fromKmToKm(double distanceKiloMeter) {
        return Math.min(distanceKiloMeter, MAX_RADIUS_DISTANCE_KM);
    }

}
