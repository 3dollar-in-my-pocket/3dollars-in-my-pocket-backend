package com.depromeet.threedollar.api.assertutils;

import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class assertReviewUtils {

    public static void assertReview(Review review, Long storeId, String contents, int rating, Long userId, ReviewStatus status) {
        assertAll(
            () -> assertThat(review.getStoreId()).isEqualTo(storeId),
            () -> assertThat(review.getUserId()).isEqualTo(userId),
            () -> assertThat(review.getContents()).isEqualTo(contents),
            () -> assertThat(review.getRating()).isEqualTo(rating),
            () -> assertThat(review.getStatus()).isEqualTo(status)
        );
    }

    public static void assertReviewInfoResponse(ReviewInfoResponse response, Review review) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(review.getStoreId()),
            () -> assertThat(response.getContents()).isEqualTo(review.getContents()),
            () -> assertThat(response.getRating()).isEqualTo(review.getRating())
        );
    }

    public static void assertReviewInfoResponse(ReviewInfoResponse response, Long storeId, String contents, int rating) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(storeId),
            () -> assertThat(response.getContents()).isEqualTo(contents),
            () -> assertThat(response.getRating()).isEqualTo(rating)
        );
    }

    public static void assertReviewWithWriterResponse(ReviewWithUserResponse response, Review review) {
        assertAll(
            () -> assertThat(response.getReviewId()).isEqualTo(review.getId()),
            () -> assertThat(response.getContents()).isEqualTo(review.getContents()),
            () -> assertThat(response.getRating()).isEqualTo(review.getRating())
        );
    }

}
