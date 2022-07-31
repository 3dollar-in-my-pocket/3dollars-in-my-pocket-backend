package com.depromeet.threedollar.api.userservice.service.review;

import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.api.userservice.service.user.UserServiceHelper;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.collection.ReviewCursorPaging;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewRetrieveService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ReviewsCursorResponse retrieveMyReviewHistories(RetrieveMyReviewsRequest request, Long userId) {
        User user = UserServiceHelper.findUserById(userRepository, userId);
        ReviewCursorPaging reviewCursorPaging = reviewRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize());
        StoreDictionary storeDictionary = StoreServiceHelper.findStoreDictionary(storeRepository, reviewCursorPaging.getStoreIds());
        return ReviewsCursorResponse.of(reviewCursorPaging, storeDictionary, user);
    }

}
