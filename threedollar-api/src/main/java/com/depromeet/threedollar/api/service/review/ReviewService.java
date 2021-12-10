package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.event.review.ReviewChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_REVIEWS_COUNTS;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ApplicationEventPublisher eventPublisher;

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @CacheEvict(key = "#userId", value = USER_REVIEWS_COUNTS)
    @Transactional
    public ReviewInfoResponse addReview(AddReviewRequest request, Long userId) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, request.getStoreId());
        Review review = reviewRepository.save(request.toEntity(userId));
        eventPublisher.publishEvent(ReviewChangedEvent.of(store));
        return ReviewInfoResponse.of(review);
    }

    @Transactional
    public ReviewInfoResponse updateReview(Long reviewId, UpdateReviewRequest request, Long userId) {
        Review review = ReviewServiceUtils.findReviewByIdAndUserId(reviewRepository, reviewId, userId);
        Store store = StoreServiceUtils.findStoreById(storeRepository, review.getStoreId());
        review.update(request.getContents(), request.getRating());
        eventPublisher.publishEvent(ReviewChangedEvent.of(store));
        return ReviewInfoResponse.of(review);
    }

    @CacheEvict(key = "#userId", value = USER_REVIEWS_COUNTS)
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = ReviewServiceUtils.findReviewByIdAndUserId(reviewRepository, reviewId, userId);
        Store store = StoreServiceUtils.findStoreById(storeRepository, review.getStoreId());
        review.delete();
        eventPublisher.publishEvent(ReviewChangedEvent.of(store));
    }

}
