package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.domain.review.ReviewRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOTFOUND_REVIEW;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReviewServiceUtils {

    @NotNull
    static Review findReviewByIdAndUserId(ReviewRepository reviewRepository, Long reviewId, Long userId) {
        Review review = reviewRepository.findReviewByIdAndUserId(reviewId, userId);
        if (review == null) {
            throw new NotFoundException(String.format("해당하는 리뷰 (%s)는 존재하지 않습니다", reviewId), NOTFOUND_REVIEW);
        }
        return review;
    }

}
