package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationDistanceUtilsTest {

    @Test
    void 두_지점간의_거리를_계산한다() {
        // given
        double sourceLatitude = 34;
        double sourceLongitude = 124;

        double targetLatitude = 35;
        double targetLongitude = 124;

        // when
        int result = LocationDistanceUtils.getDistance(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

        // then
        assertThat(result).isEqualTo(111189);
    }

    @Test
    void 두_지점간의_거리를_계산한다2() {
        // given
        double sourceLatitude = 34;
        double sourceLongitude = 124;

        double targetLatitude = 34;
        double targetLongitude = 124;

        // when
        int result = LocationDistanceUtils.getDistance(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

        // then
        assertThat(result).isEqualTo(0);
    }

}
