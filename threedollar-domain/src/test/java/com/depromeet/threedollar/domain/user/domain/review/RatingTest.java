package com.depromeet.threedollar.domain.user.domain.review;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RatingTest {

    @ValueSource(ints = {1, 2, 3, 4, 5})
    @ParameterizedTest
    void 점수가_1점이상_5점이하면_통과한다(int value) {
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
