package com.depromeet.threedollar.api.user.controller.review.support;

import static com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions.assertStoreInfoResponse;
import static com.depromeet.threedollar.api.user.controller.user.support.UserAssertions.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewDetailResponse;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.TestHelper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewAssertions {

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
