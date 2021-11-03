package com.depromeet.threedollar.domain.domain.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceGroupTypeTest {

    @ParameterizedTest
    @ValueSource(doubles = {0, 49.9, -1})
    void UNDER_FIFTY_50미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.UNDER_FIFTY);
    }

    @ParameterizedTest
    @ValueSource(doubles = {50, 70, 99})
    void FIFTY_TO_HUNDRED_50이상_100미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.FIFTY_TO_HUNDRED);
    }

    private static Stream<Arguments> source_fifty_to_hundred() {
        return Stream.of(
            Arguments.of(50),
            Arguments.of(99)
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 300, 499})
    void HUNDRED_TO_FIVE_HUNDRED_100이상_500미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.HUNDRED_TO_FIVE_HUNDRED);
    }

    @ParameterizedTest
    @ValueSource(doubles = {500, 700, 999})
    void FIVE_HUNDRED_TO_THOUSAND_500이상_1000미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.FIVE_HUNDRED_TO_THOUSAND);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000, 1500, 2000})
    void OVER_THOUSAND_1000이상인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.OVER_THOUSAND);
    }

}
