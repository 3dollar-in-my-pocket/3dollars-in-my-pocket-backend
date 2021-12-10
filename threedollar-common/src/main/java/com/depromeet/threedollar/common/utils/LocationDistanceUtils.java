package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationDistanceUtils {

    private static final int STRAIGHT_ANGLE = 180;

    /**
     * 두 위도/경도간의 거리를 계산해주는 유틸성 메소드.
     * 일단 기존의 프로젝트의 방법 적용하였음.
     */
    public static int getDistance(Double sourceLatitude, Double sourceLongitude, Double targetLatitude, Double targetLongitude) {
        if (sourceLatitude == null || sourceLongitude == null || targetLatitude == null || targetLongitude == null) {
            return 0;
        }
        double theta = sourceLongitude - targetLongitude;
        double dist = Math.sin(convertDegreeToRadian(sourceLatitude)) * Math.sin(convertDegreeToRadian(targetLatitude))
            + Math.cos(convertDegreeToRadian(sourceLatitude)) * Math.cos(convertDegreeToRadian(targetLatitude)) * Math.cos(convertDegreeToRadian(theta));
        dist = Math.acos(dist);
        dist = convertRadinToDegree(dist);
        dist = dist * 60 * 1.1515;
        return (int) (dist * 1609.344);
    }

    private static double convertDegreeToRadian(double degree) {
        return (degree * Math.PI / STRAIGHT_ANGLE);
    }

    private static double convertRadinToDegree(double radian) {
        return (radian * STRAIGHT_ANGLE / Math.PI);
    }

}
