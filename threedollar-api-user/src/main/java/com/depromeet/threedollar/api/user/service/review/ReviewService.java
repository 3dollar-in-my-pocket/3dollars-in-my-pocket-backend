package com.depromeet.threedollar.api.user.service.review;

import com.depromeet.threedollar.api.user.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.user.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.user.service.store.StoreServiceUtils;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_REVIEWS_COUNTS;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @CacheEvict(key = "#userId", value = USER_REVIEWS_COUNTS)
    @Transactional
    public ReviewInfoResponse addReview(AddReviewRequest request, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        Review review = reviewRepository.save(request.toEntity(userId));
        return ReviewInfoResponse.of(review);
    }

    @Transactional
    public ReviewInfoResponse updateReview(Long reviewId, UpdateReviewRequest request, Long userId) {
        Review review = ReviewServiceUtils.findReviewByIdAndUserId(reviewRepository, reviewId, userId);
        review.update(request.getContents(), request.getRating());
        return ReviewInfoResponse.of(review);
    }

    @CacheEvict(key = "#userId", value = USER_REVIEWS_COUNTS)
    @Transactional
    public ReviewInfoResponse deleteReview(Long reviewId, Long userId) {
        Review review = ReviewServiceUtils.findReviewByIdAndUserId(reviewRepository, reviewId, userId);
        review.delete();
        return ReviewInfoResponse.of(review);
    }

}
