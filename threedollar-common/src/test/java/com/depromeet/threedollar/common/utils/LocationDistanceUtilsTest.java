package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class LocationDistanceUtilsTest {

    @CsvSource({
        "34, 124, 35, 124, 111189",
        "34, 124, 34, 124, 0"
    })
    @ParameterizedTest
    void 두_지점간의_거리를_계산한다(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude, int distance) {
        // when
        int result = LocationDistanceUtils.getDistance(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

        // then
        assertThat(result).isEqualTo(distance);
    }

}
