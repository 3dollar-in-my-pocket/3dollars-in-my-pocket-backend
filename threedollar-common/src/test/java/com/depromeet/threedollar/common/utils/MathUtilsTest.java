package com.depromeet.threedollar.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MathUtilsTest {

    @Nested
    class RoundTest {

        @ValueSource(doubles = {3.5, 3.99999})
        @ParameterizedTest
        void 소수점_첫째_자리에서_반올림한다_올림인경우(double value) {
            // when
            double result = MathUtils.round(value, 0);

            // then
            assertThat(result).isEqualTo(4);
        }

        @ValueSource(doubles = {3.85, 3.87, 3.89})
        @ParameterizedTest
        void 소수점_둘쨰_자리에서_반올림한다_올림인경우(double value) {
            // when
            double result = MathUtils.round(value, 1);

            // then
            assertThat(result).isEqualTo(3.9);
        }

        @ValueSource(doubles = {3.7, 3.74999})
        @ParameterizedTest
        void 소수점_둘쨰_자리에서_반올림한다_내림인_경우(double value) {
            // when
            double result = MathUtils.round(value, 1);

            // then
            assertThat(result).isEqualTo(3.7);
        }

        @Test
        void ZERO_인경우_0이된다() {
            // given
            double value = 0.0;

            // when
            double result = MathUtils.round(value, 1);

            // then
            assertThat(result).isZero();
        }

    }

    @Nested
    class DivideTest {

        @Test
        void 정수를_정수로_나누는경우_실수로_변환되서_나눠진다() {
            // given
            int dividend = 1;
            int divisor = 2;

            // when
            double result = MathUtils.divide(dividend, divisor);

            // then
            assertThat(result).isEqualTo(0.5);
        }

        @Test
        void 실수를_실수로_나누는경우_실수로_나뉘어진다() {
            // given
            double dividend = 1.0;
            double divisor = 2.0;

            // when
            double result = MathUtils.divide(dividend, divisor);

            // then
            assertThat(result).isEqualTo(0.5);
        }

        @Test
        void 특정_수를_0으로_나누는경우_0이된다() {
            // given
            double dividend = 1;
            double divisor = 0;

            // when
            double result = MathUtils.divide(dividend, divisor);

            // then
            assertThat(result).isZero();
        }

    }

}
