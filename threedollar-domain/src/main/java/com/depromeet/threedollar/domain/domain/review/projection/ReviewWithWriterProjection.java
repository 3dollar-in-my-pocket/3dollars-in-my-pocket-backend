package com.depromeet.threedollar.domain.domain.review.projection;

import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@ToString
@Getter
public class ReviewWithWriterProjection {

    private final Long reviewId;
    private final int rating;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final Long storeId;

    @Nullable
    private final Long userId;

    @Nullable
    private final String userName;

    @Nullable
    private final UserSocialType userSocialType;

    @QueryProjection
    public ReviewWithWriterProjection(Long reviewId, int rating, String contents, LocalDateTime createdAt, LocalDateTime updatedAt,
                                      Long storeId, @Nullable Long userId, @Nullable String userName, @Nullable UserSocialType userSocialType) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.userSocialType = userSocialType;
    }

}
