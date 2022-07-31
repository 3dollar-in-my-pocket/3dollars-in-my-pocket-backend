package com.depromeet.threedollar.api.userservice.service.review;

import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.api.userservice.service.user.UserServiceHelper;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.collection.ReviewPagingCursor;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewRetrieveService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ReviewsCursorResponse retrieveMyReviewHistories(RetrieveMyReviewsRequest request, Long userId) {
        User user = UserServiceHelper.findUserById(userRepository, userId);
        ReviewPagingCursor reviewsPagingCursor = getReviewsPagingCursor(userId, request.getCursor(), request.getSize());
        StoreDictionary storeDictionary = StoreServiceHelper.findStoreDictionary(storeRepository, reviewsPagingCursor.getStoreIds());
        return ReviewsCursorResponse.of(reviewsPagingCursor, storeDictionary, user);
    }

    private ReviewPagingCursor getReviewsPagingCursor(@NotNull Long userId, @Nullable Long cursor, int size) {
        List<Review> reviewsWithNextCursor = reviewRepository.findAllByUserIdUsingCursor(userId, cursor, size + 1);
        return ReviewPagingCursor.of(reviewsWithNextCursor, size);
    }

}
