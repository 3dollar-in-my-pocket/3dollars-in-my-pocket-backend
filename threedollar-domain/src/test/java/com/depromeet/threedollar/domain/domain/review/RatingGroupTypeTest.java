package com.depromeet.threedollar.domain.domain.review;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class RatingGroupTypeTest {

    @ValueSource(doubles = {-0.1, 0, 0.9})
    @ParameterizedTest
    void ZERO_TO_ONE_0점이상_1점미만(double rating) {
        // when
        RatingGroupType reviewGroup = RatingGroupType.of(rating);

        // then
        assertThat(reviewGroup).isEqualTo(RatingGroupType.ZERO_TO_ONE);
    }

    @ValueSource(doubles = {1, 1.9})
    @ParameterizedTest
    void ZERO_TO_ONE_1점이상_2점미만(double rating) {
        // when
        RatingGroupType reviewGroup = RatingGroupType.of(rating);

        // then
        assertThat(reviewGroup).isEqualTo(RatingGroupType.ONE_TO_TWO);
    }

    @ValueSource(doubles = {2, 2.9})
    @ParameterizedTest
    void TWO_TO_THREE_2점이상_3점미만(double rating) {
        // when
        RatingGroupType reviewGroup = RatingGroupType.of(rating);

        // then
        assertThat(reviewGroup).isEqualTo(RatingGroupType.TWO_TO_THREE);
    }

    @ValueSource(doubles = {3, 3.9})
    @ParameterizedTest
    void THREE_TO_FOUR_3점이상_4점미만(double rating) {
        // when
        RatingGroupType reviewGroup = RatingGroupType.of(rating);

        // then
        assertThat(reviewGroup).isEqualTo(RatingGroupType.THREE_TO_FOUR);
    }

    @ValueSource(doubles = {4, 5, 5.1})
    @ParameterizedTest
        // 5점 이상의 예상치 못한 범위가 온 경우 에러보다 이 방법 선택.
    void FOUR_TO_FIVE_4점이상만(double rating) {
        // when
        RatingGroupType reviewGroup = RatingGroupType.of(rating);

        // then
        assertThat(reviewGroup).isEqualTo(RatingGroupType.FOUR_TO_FIVE);
    }

}
