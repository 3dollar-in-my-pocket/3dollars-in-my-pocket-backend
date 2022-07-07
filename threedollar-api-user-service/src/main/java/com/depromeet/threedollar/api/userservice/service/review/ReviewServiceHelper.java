package com.depromeet.threedollar.api.userservice.service.review;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOT_FOUND_REVIEW;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReviewServiceHelper {

    @NotNull
    static Review findReviewByIdAndUserId(ReviewRepository reviewRepository, Long reviewId, Long userId) {
        Review review = reviewRepository.findReviewByIdAndUserId(reviewId, userId);
        if (review == null) {
            throw new NotFoundException(String.format("해당하는 리뷰(%s)는 존재하지 않습니다", reviewId), NOT_FOUND_REVIEW);
        }
        return review;
    }

}
