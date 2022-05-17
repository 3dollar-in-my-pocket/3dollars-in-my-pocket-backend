package com.depromeet.threedollar.common.utils.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LookupRadiusDistanceLimiterTest {

    @Test
    void 최대_KM_제한을_넘는경우_최대_KM로_변환된다() {
        // given
        double km = 3.0;

        double result = LookupRadiusDistanceLimiter.fromKmToKm(km);

        // then
        assertThat(result).isEqualTo(2.0);
    }

    @Test
    void 최대_KM_제한을_넘지않는경우_기존_KM_가_유지된다() {
        // given
        double km = 1.9;

        double result = LookupRadiusDistanceLimiter.fromKmToKm(km);

        // then
        assertThat(result).isEqualTo(1.9);
    }

    @Test
    void 제한_미터를_넘지_않는경우_KM로_변환해서_유지된다() {
        // given
        double m = 1900.0;

        double result = LookupRadiusDistanceLimiter.fromMtoKm(m);

        // then
        assertThat(result).isEqualTo(1.9);
    }

}
