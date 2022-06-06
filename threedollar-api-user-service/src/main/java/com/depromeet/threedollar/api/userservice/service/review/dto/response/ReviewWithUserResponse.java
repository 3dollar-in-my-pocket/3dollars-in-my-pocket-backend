package com.depromeet.threedollar.api.userservice.service.review.dto.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        response.setAuditingTimeByEntity(review);
        return response;
    }

}