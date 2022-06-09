package com.depromeet.threedollar.api.userservice.service.review;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_REVIEWS_COUNTS;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.userservice.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.userservice.service.store.StoreServiceUtils;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @CacheEvict(cacheNames = USER_REVIEWS_COUNTS, key = "#userId")
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

    @CacheEvict(cacheNames = USER_REVIEWS_COUNTS, key = "#userId")
    @Transactional
    public ReviewInfoResponse deleteReview(Long reviewId, Long userId) {
        Review review = ReviewServiceUtils.findReviewByIdAndUserId(reviewRepository, reviewId, userId);
        review.delete();
        return ReviewInfoResponse.of(review);
    }

}
