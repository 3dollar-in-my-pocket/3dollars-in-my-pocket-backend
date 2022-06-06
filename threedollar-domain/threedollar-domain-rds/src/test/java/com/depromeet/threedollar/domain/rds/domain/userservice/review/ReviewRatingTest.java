package com.depromeet.threedollar.domain.rds.domain.userservice.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.depromeet.threedollar.common.exception.model.InvalidException;

class ReviewRatingTest {

    @ValueSource(ints = {1, 2, 3, 4, 5})
    @ParameterizedTest
    void 리뷰_점수가_1점에서_5점사이면_유효성_검증을_통과한다(int value) {
        // when
        ReviewRating reviewRating = ReviewRating.of(value);

        // then
        assertThat(reviewRating.getRating()).isEqualTo(value);
    }

    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void 점수가_1보다_작거나_5이상이면_VALIDATION_EXEPTION(int value) {
        // when & then
        assertThatThrownBy(() -> ReviewRating.of(value)).isInstanceOf(InvalidException.class);
    }

}
