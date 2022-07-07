package com.depromeet.threedollar.domain.rds.domain.userservice.review;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_RATING_RANGE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class ReviewRating {

    private static final int MIN_RATING_VALUE = 1;
    private static final int MAX_RATING_VALUE = 5;

    @Column(nullable = false)
    private int rating;

    private ReviewRating(int rating) {
        validateRatingInAvailableRange(rating);
        this.rating = rating;
    }

    public static ReviewRating of(int rating) {
        return new ReviewRating(rating);
    }

    private void validateRatingInAvailableRange(int rating) {
        if (rating < MIN_RATING_VALUE || rating > MAX_RATING_VALUE) {
            throw new InvalidException(String.format("허용하는 리뷰 평가 점수(%s) 범위를 벗어났습니다. 허용 범위: (%s ~ %s)", rating, MIN_RATING_VALUE, MAX_RATING_VALUE), INVALID_RATING_RANGE);
        }
    }

}
