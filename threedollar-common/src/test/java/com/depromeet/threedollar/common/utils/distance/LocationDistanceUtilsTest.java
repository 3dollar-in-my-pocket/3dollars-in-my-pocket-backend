package com.depromeet.threedollar.common.utils.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.depromeet.threedollar.common.model.LocationValue;

class LocationDistanceUtilsTest {

    @CsvSource({
        "34, 126, 35, 126, 111189",
        "34, 126, 34, 126, 0"
    })
    @ParameterizedTest
    void 두_지점간의_거리를_계산한다(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude, int distance) {
        // when
        int result = LocationDistanceUtils.getDistanceM(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

        // then
        assertThat(result).isEqualTo(distance);
    }

    @CsvSource({
        "34, 126, 35, 0",
        "34, 126, 0, 126",
        "34, 0, 35, 126",
        "0, 126, 35, 126"
    })
    @ParameterizedTest
    void 어느하나_0인경우_마이너스1을_반환한다(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude) {
        // when
        int result = LocationDistanceUtils.getDistanceM(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

        // then
        assertThat(result).isEqualTo(-1);
    }

    @CsvSource({
        "34, 126, 35, 126, 111189",
        "34, 126, 34, 126, 0"
    })
    @ParameterizedTest
    void 두_Location_지점_간의_거리를_계산한다(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude, int distance) {
        // when
        int result = LocationDistanceUtils.getDistanceM(LocationValue.of(sourceLatitude, sourceLongitude), LocationValue.of(targetLatitude, targetLongitude));

        // then
        assertThat(result).isEqualTo(distance);
    }

    @Test
    void source_위치_정보가_null인경우_마이너스1을_반환한다() {
        // when
        int result = LocationDistanceUtils.getDistanceM(null, LocationValue.of(38.0, 124.0));

        // then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void target_위치_정보가_null인경우_마이너스1을_반환한다() {
        // when
        int result = LocationDistanceUtils.getDistanceM(LocationValue.of(38.0, 124.0), null);

        // then
        assertThat(result).isEqualTo(-1);
    }

}
