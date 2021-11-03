package com.depromeet.threedollar.domain.domain.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceGroupTypeTest {

    @ValueSource(doubles = {0, 49.9, -1})
    @ParameterizedTest
    void UNDER_FIFTY_50미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.UNDER_FIFTY);
    }

    @ValueSource(doubles = {50, 70, 99.99})
    @ParameterizedTest
    void FIFTY_TO_HUNDRED_50이상_100미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.FIFTY_TO_HUNDRED);
    }

    @ValueSource(doubles = {100, 300, 499.99})
    @ParameterizedTest
    void HUNDRED_TO_FIVE_HUNDRED_100이상_500미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.HUNDRED_TO_FIVE_HUNDRED);
    }

    @ValueSource(doubles = {500, 700, 999.99})
    @ParameterizedTest
    void FIVE_HUNDRED_TO_THOUSAND_500이상_1000미만인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.FIVE_HUNDRED_TO_THOUSAND);
    }

    @ValueSource(doubles = {1000, 1500, 2000})
    @ParameterizedTest
    void OVER_THOUSAND_1000이상인경우(double distance) {
        // when
        DistanceGroupType type = DistanceGroupType.of(distance);

        // then
        assertThat(type).isEqualTo(DistanceGroupType.OVER_THOUSAND);
    }

}
