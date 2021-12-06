package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.request.deprecated.RetrieveMyReviewsV2Request;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewScrollResponse;
import com.depromeet.threedollar.api.service.review.dto.response.deprecated.ReviewScrollV2Response;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.StoreCollection;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewRetrieveService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ReviewScrollResponse retrieveMyReviewHistories(RetrieveMyReviewsRequest request, Long userId) {
        List<Review> reviewsWithNextCursor = reviewRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Review> reviewsScrollCollection = ScrollPaginationCollection.of(reviewsWithNextCursor, request.getSize());
        return ReviewScrollResponse.of(
            reviewsScrollCollection,
            findStoresByReviews(reviewsScrollCollection.getItemsInCurrentScroll()),
            UserServiceUtils.findUserById(userRepository, userId)
        );
    }

    @Deprecated
    @Transactional(readOnly = true)
    public ReviewScrollV2Response retrieveMyReviewHistoriesV2(RetrieveMyReviewsV2Request request, Long userId) {
        List<Review> reviewsWithNextCursor = reviewRepository.findAllActiveByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Review> reviewScrollCollection = ScrollPaginationCollection.of(reviewsWithNextCursor, request.getSize());
        return ReviewScrollV2Response.of(
            reviewScrollCollection,
            findStoresByReviews(reviewScrollCollection.getItemsInCurrentScroll()),
            UserServiceUtils.findUserById(userRepository, userId),
            Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> reviewRepository.findActiveCountsByUserId(userId))
        );
    }

    private StoreCollection findStoresByReviews(List<Review> reviews) {
        List<Long> storeIds = reviews.stream()
            .map(Review::getStoreId)
            .collect(Collectors.toList());
        return StoreCollection.of(storeRepository.findAllByIds(storeIds));
    }

}
