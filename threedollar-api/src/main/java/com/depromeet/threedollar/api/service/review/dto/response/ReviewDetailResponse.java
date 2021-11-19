package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponse {

    private ReviewInfoResponse review;

    private StoreInfoResponse store;

    private UserInfoResponse user;

    public static ReviewDetailResponse of(ReviewWithWriterProjection review, Store store) {
        return new ReviewDetailResponse(
            ReviewInfoResponse.of(review),
            StoreInfoResponse.ofZeroVisitCounts(store),
            UserInfoResponse.of(review.getUserId(), review.getUserName(), review.getUserSocialType())
        );
    }

}
