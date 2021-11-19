package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationDistanceUtils {

    /**
     * 두 위도/경도간의 거리를 계산해주는 유틸성 메소드.
     * 일단 기존의 프로젝트의 방법 적용하였음.
     */
    public static int getDistance(Double sourceLatitude, Double sourceLongitude, Double targetLatitude, Double targetLongitude) {
        if (sourceLatitude == null || sourceLongitude == null || targetLatitude == null || targetLongitude == null) {
            return 0;
        }
        double theta = sourceLongitude - targetLongitude;
        double dist = Math.sin(deg2rad(sourceLatitude)) * Math.sin(deg2rad(targetLatitude))
            + Math.cos(deg2rad(sourceLatitude)) * Math.cos(deg2rad(targetLatitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (int) (dist * 1609.344);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
