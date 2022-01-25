package com.depromeet.threedollar.testhelper.assertion;

import com.depromeet.threedollar.api.service.review.dto.response.ReviewDetailResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.domain.user.domain.TestHelper;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.domain.review.ReviewStatus;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import com.depromeet.threedollar.domain.user.domain.user.User;

import static com.depromeet.threedollar.testhelper.assertion.StoreAssertionHelper.assertStoreInfoResponse;
import static com.depromeet.threedollar.testhelper.assertion.UserAssertionHelper.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestHelper
public final class ReviewAssertionHelper {

    public static void assertReview(Review review, Long storeId, String contents, int rating, Long userId, ReviewStatus status) {
        assertAll(
            () -> assertThat(review.getStoreId()).isEqualTo(storeId),
            () -> assertThat(review.getUserId()).isEqualTo(userId),
            () -> assertThat(review.getContents()).isEqualTo(contents),
            () -> assertThat(review.getRating()).isEqualTo(rating),
            () -> assertThat(review.getStatus()).isEqualTo(status)
        );
    }

    public static void assertReviewDetailInfoResponse(ReviewDetailResponse response, Review review, Store store, User user) {
        assertAll(
            () -> assertThat(response.getReviewId()).isEqualTo(review.getId()),
            () -> assertThat(response.getRating()).isEqualTo(review.getRating()),
            () -> assertThat(response.getContents()).isEqualTo(review.getContents()),
            () -> assertStoreInfoResponse(response.getStore(), store),
            () -> assertUserInfoResponse(response.getUser(), user)
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