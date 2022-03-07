package com.depromeet.threedollar.api.user.service.review.dto.response;

import com.depromeet.threedollar.api.user.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewWithUserResponse extends AuditingTimeResponse {

    private Long reviewId;

    private int rating;

    @Nullable
    private String contents;

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewWithUserResponse(Long reviewId, int rating, @Nullable String contents, UserInfoResponse user) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.user = user;
    }

    public static ReviewWithUserResponse of(@NotNull Review review, @Nullable User user) {
        ReviewWithUserResponse response = ReviewWithUserResponse.builder()
            .reviewId(review.getId())
            .rating(review.getRating())
            .contents(review.getContents())
            .user(UserInfoResponse.of(user))
            .build();
        response.setBaseTime(review);
        return response;
    }

}
