package com.depromeet.threedollar.common.utils.distance;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceUnitConvertUtilsTest {

    @Test
    void 킬로미터_단위를_미터로_변환한다() {
        // given
        double m = 1202;

        // when
        double sut = DistanceUnitConvertUtils.fromMeterToKm(m);

        // then
        assertThat(sut).isEqualTo(1.202);
    }

}
