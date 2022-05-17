package com.depromeet.threedollar.common.utils;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.model.LocationValue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationDistanceUtils {

    private static final int UNKNOWN_DISTANCE = -1;
    private static final int STRAIGHT_ANGLE = 180;

    /**
     * 두 위도/경도간의 거리를 계산해주는 유틸성 메소드.
     */
    public static int getDistance(@Nullable LocationValue source, @Nullable LocationValue target) {
        if (source == null || target == null) {
            return UNKNOWN_DISTANCE;
        }
        return getDistance(source.getLatitude(), source.getLongitude(), target.getLatitude(), target.getLongitude());
    }

    public static int getDistance(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude) {
        if (sourceLatitude == 0 || sourceLongitude == 0 || targetLatitude == 0 || targetLongitude == 0) {
            return UNKNOWN_DISTANCE;
        }
        double theta = sourceLongitude - targetLongitude;
        double dist = Math.sin(convertDegreeToRadian(sourceLatitude)) * Math.sin(convertDegreeToRadian(targetLatitude))
            + Math.cos(convertDegreeToRadian(sourceLatitude)) * Math.cos(convertDegreeToRadian(targetLatitude)) * Math.cos(convertDegreeToRadian(theta));
        dist = Math.acos(dist);
        dist = convertRadianToDegree(dist);
        dist = dist * 60 * 1.1515;
        return (int) (dist * 1609.344);
    }

    private static double convertDegreeToRadian(double degree) {
        return (degree * Math.PI / STRAIGHT_ANGLE);
    }

    private static double convertRadianToDegree(double radian) {
        return (radian * STRAIGHT_ANGLE / Math.PI);
    }

}
