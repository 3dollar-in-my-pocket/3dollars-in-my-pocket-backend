package com.depromeet.threedollar.api.vendor.service.review.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.review.Review;
import com.depromeet.threedollar.domain.rds.vendor.domain.review.ReviewStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewAssertions {

    public static void assertReview(Review review, Long storeId, String contents, int rating, Long userId, ReviewStatus status) {
        assertAll(
            () -> assertThat(review.getStoreId()).isEqualTo(storeId),
            () -> assertThat(review.getUserId()).isEqualTo(userId),
            () -> assertThat(review.getContents()).isEqualTo(contents),
            () -> assertThat(review.getRating()).isEqualTo(rating),
            () -> assertThat(review.getStatus()).isEqualTo(status)
        );
    }

}
