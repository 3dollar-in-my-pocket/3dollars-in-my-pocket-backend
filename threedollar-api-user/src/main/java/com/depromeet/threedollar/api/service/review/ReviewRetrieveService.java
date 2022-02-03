package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.common.collection.CursorSupporter;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.user.collection.store.StoreDictionary;
import com.depromeet.threedollar.domain.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.user.domain.user.User;
import com.depromeet.threedollar.domain.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private StoreDictionary findStoresByReviews(List<Review> reviews) {
        List<Long> storeIds = reviews.stream()
            .map(Review::getStoreId)
            .collect(Collectors.toList());
        return StoreDictionary.of(storeRepository.findAllByIds(storeIds));
    }

}
