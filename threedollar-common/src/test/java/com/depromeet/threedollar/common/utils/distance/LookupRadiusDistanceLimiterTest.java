package com.depromeet.threedollar.common.utils.distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.common.exception.model.InvalidException;

class LookupRadiusDistanceLimiterTest {

    @Nested
    class 주변_반경거리_제한_KM {

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
        void KM가_0보다_작은경우_InvalidException() {
            // given
            double km = -0.1;

            // when & then
            assertThatThrownBy(() -> LookupRadiusDistanceLimiter.fromKmToKm(km)).isInstanceOf(InvalidException.class);
        }

    }

    @Nested
    class 주변_반경거리_제한_M {

        @Test
        void 제한_반경거리를_넘는경우_최대_반경_거리로_설정된다() {
            // given
            double m = 2001.0;

            double result = LookupRadiusDistanceLimiter.fromMtoKm(m);

            // then
            assertThat(result).isEqualTo(2.0);
        }

        @Test
        void 최대_KM_제한을_넘지않는경우_기존_KM_가_유지된다() {
            // given
            double km = 1900;

            double result = LookupRadiusDistanceLimiter.fromMtoKm(km);

            // then
            assertThat(result).isEqualTo(1.9);
        }

        @Test
        void KM가_0보다_작은경우_InvalidException() {
            // given
            double km = -100;

            // when & then
            assertThatThrownBy(() -> LookupRadiusDistanceLimiter.fromMtoKm(km)).isInstanceOf(InvalidException.class);
        }

    }

}
