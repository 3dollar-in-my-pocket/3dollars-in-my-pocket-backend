package com.depromeet.threedollar.domain.rds.user.domain.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreRadiusDistance {

    private static final double MAX_RADIUS_DISTANCE = 2.0;

    private final double distance;

    public static StoreRadiusDistance of(double distance) {
        return new StoreRadiusDistance(distance);
    }

    public double getAvailableDistance() {
        return Math.min(distance, MAX_RADIUS_DISTANCE);
    }

}