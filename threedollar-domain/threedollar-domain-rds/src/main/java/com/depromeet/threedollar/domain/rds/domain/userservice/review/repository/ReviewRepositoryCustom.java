package com.depromeet.threedollar.domain.rds.domain.userservice.review.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ReviewRepositoryCustom {

    @Nullable
    Review findReviewById(Long reviewId);

    @Nullable
    Review findReviewByIdAndUserId(Long reviewId, Long userId);

    List<Review> findAllByStoreId(Long storeId);

    List<Review> findAllByStoreIdWithLock(Long storeId);

    long countByUserId(Long userId);

    List<Review> findAllByUserIdUsingCursor(Long userId, @Nullable Long lastStoreId, int size);

}
