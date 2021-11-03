package com.depromeet.threedollar.domain.domain.review;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RatingTest {

    @Nested
    class 평가점수로_인스턴스_생성 {

        @ValueSource(ints = {1, 2, 3, 4, 5})
        @ParameterizedTest
        void 평가_점수_값_객체가_생성된다(int value) {
            // when
            Rating rating = Rating.of(value);

            // then
            assertThat(rating.getRating()).isEqualTo(value);
        }

        @ValueSource(ints = {0, 6})
        @ParameterizedTest
        void 점수가_1보다_작거나_5이상이면_VALIDATION_EXEPTION(int value) {
            // when & then
            assertThatThrownBy(() -> Rating.of(value)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    class 동등성_테스트 {

        @ValueSource(ints = {1, 2, 3, 4, 5})
        @ParameterizedTest
        void 평가점수가_같은경우_같은_객체로_판단한다(int score) {
            // when
            Rating rating = Rating.of(score);

            // then
            assertThat(rating).isEqualTo(Rating.of(score));
        }

        @Test
        void 평가점수가_다른_값일경우_다른_객체로_판단한다() {
            // when
            Rating rating = Rating.of(4);

            // then
            assertThat(rating).isNotEqualTo(Rating.of(3));
        }

    }

}
