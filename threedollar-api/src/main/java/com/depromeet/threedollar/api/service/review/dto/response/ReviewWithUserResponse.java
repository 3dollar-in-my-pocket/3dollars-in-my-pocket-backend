package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.user.User;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
