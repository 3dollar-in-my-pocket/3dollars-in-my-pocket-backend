package com.depromeet.threedollar.api.userservice.service.review;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.api.userservice.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;

import lombok.RequiredArgsConstructor;

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
        CursorPagingSupporter<Review> reviewsCursor = CursorPagingSupporter.of(reviewsWithNextCursor, request.getSize());
        StoreDictionary storeDictionary = findStoresByReviews(reviewsCursor.getCurrentCursorItems());
        return ReviewsCursorResponse.of(reviewsCursor, storeDictionary, user);
    }

    private StoreDictionary findStoresByReviews(List<Review> reviews) {
        List<Long> storeIds = reviews.stream()
            .map(Review::getStoreId)
            .collect(Collectors.toList());
        return StoreDictionary.of(storeRepository.findAllByIds(storeIds));
    }

}
