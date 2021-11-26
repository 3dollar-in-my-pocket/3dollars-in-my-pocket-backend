package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewWithUserResponse extends AuditingTimeResponse {

    private Long reviewId;
    private int rating;
    private String contents;

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewWithUserResponse(Long reviewId, int rating, String contents, UserInfoResponse user) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.user = user;
    }

    public static ReviewWithUserResponse of(ReviewWithWriterProjection projection) {
        ReviewWithUserResponse response = ReviewWithUserResponse.builder()
            .reviewId(projection.getReviewId())
            .rating(projection.getRating())
            .contents(projection.getContents())
            .user(UserInfoResponse.of(projection.getUserId(), projection.getUserName(), projection.getUserSocialType()))
            .build();
        response.setBaseTime(projection.getCreatedAt(), projection.getUpdatedAt());
        return response;
    }

}
