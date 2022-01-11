package com.depromeet.threedollar.domain.user.domain.review.repository;

import com.depromeet.threedollar.domain.user.domain.review.Review;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ReviewRepositoryCustom {

    @Nullable
    Review findReviewByIdAndUserId(Long reviewId, Long userId);

    List<Review> findAllByStoreId(Long storeId);

    List<Review> findAllByStoreIdWithLock(Long storeId);

    long findCountsByUserId(Long userId);

    @Deprecated
    long findActiveCountsByUserId(Long userId);

    List<Review> findAllByUserIdUsingCursor(Long userId, Long lastStoreId, int size);

    @Deprecated
    List<Review> findAllActiveByUserIdUsingCursor(Long userId, Long lastStoreId, int size);

}