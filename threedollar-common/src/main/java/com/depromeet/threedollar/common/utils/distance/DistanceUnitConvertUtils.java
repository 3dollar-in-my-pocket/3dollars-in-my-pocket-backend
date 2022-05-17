package com.depromeet.threedollar.common.utils.distance;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DistanceUnitConvertUtils {

    private static final int MULTIPLES_KM_TO_M = 1000;

    public static double fromMeterToKm(double meter) {
        return meter / MULTIPLES_KM_TO_M;
    }

}
