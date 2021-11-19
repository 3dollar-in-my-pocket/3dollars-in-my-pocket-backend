package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponse extends AuditingTimeResponse {

    private Long reviewId;
    private int rating;
    private String contents;

    private UserInfoResponse user;

    private StoreInfoResponse store;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewDetailResponse(Long reviewId, int rating, String contents, UserInfoResponse user, StoreInfoResponse store) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.user = user;
        this.store = store;
    }

    public static ReviewDetailResponse of(ReviewWithWriterProjection review, Store store) {
        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .reviewId(review.getReviewId())
            .rating(review.getRating())
            .contents(review.getContents())
            .user(UserInfoResponse.of(review.getUserId(), review.getUserName(), review.getUserSocialType()))
            .store(StoreInfoResponse.of(store))
            .build();
        response.setBaseTime(review.getCreatedAt(), review.getUpdatedAt());
        return response;
    }

}
