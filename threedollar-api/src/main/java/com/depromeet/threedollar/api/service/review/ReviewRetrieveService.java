package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.request.deprecated.RetrieveMyReviewsV2Request;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.api.service.review.dto.response.deprecated.ReviewsCursorV2Response;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.collection.common.CursorSupporter;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.collection.store.StoreDictionary;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.User;
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
    public ReviewsCursorResponse retrieveMyReviewHistories(RetrieveMyReviewsRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Review> reviewsWithNextCursor = reviewRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorSupporter<Review> reviewsCursor = CursorSupporter.of(reviewsWithNextCursor, request.getSize());
        StoreDictionary storeDictionary = findStoresByReviews(reviewsCursor.getItemsInCurrentCursor());
        return ReviewsCursorResponse.of(reviewsCursor, storeDictionary, user);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public ReviewsCursorV2Response retrieveMyReviewHistoriesV2(RetrieveMyReviewsV2Request request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Review> reviewsWithNextCursor = reviewRepository.findAllActiveByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorSupporter<Review> reviewsCursor = CursorSupporter.of(reviewsWithNextCursor, request.getSize());
        StoreDictionary storeDictionary = findStoresByReviews(reviewsCursor.getItemsInCurrentCursor());
        return ReviewsCursorV2Response.of(reviewsCursor, storeDictionary, user, Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> reviewRepository.findActiveCountsByUserId(userId)));
    }

    private StoreDictionary findStoresByReviews(List<Review> reviews) {
        List<Long> storeIds = reviews.stream()
            .map(Review::getStoreId)
            .collect(Collectors.toList());
        return StoreDictionary.of(storeRepository.findAllByIds(storeIds));
    }

}
