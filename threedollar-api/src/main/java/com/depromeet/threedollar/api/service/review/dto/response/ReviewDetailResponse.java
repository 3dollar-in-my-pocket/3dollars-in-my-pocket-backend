package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponse {

    private ReviewInfoResponse review;
    private StoreInfoResponse store;
    private UserInfoResponse user;

    private ReviewDetailResponse(ReviewInfoResponse review, StoreInfoResponse store, UserInfoResponse user) {
        this.review = review;
        this.store = store;
        this.user = user;
    }

    public static ReviewDetailResponse of(ReviewWithWriterProjection review, Store store) {
        return new ReviewDetailResponse(
            ReviewInfoResponse.of(review),
            StoreInfoResponse.ofZeroVisitCounts(store),
            UserInfoResponse.of(review.getUserId(), review.getUserName(), review.getUserSocialType())
        );
    }

}
