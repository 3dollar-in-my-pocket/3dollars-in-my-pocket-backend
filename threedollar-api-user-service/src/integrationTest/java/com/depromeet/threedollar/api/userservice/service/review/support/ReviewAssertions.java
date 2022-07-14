package com.depromeet.threedollar.api.userservice.service.review.support;

import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestAssertions
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
