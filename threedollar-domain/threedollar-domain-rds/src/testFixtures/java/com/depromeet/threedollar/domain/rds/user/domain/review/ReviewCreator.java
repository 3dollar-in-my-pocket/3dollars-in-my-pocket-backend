package com.depromeet.threedollar.domain.rds.user.domain.review;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class ReviewCreator {

    @Builder
    public static Review create(
        @NotNull Long storeId,
        @NotNull Long userId,
        @NotNull String contents,
        int rating,
        ReviewStatus status
    ) {
        if (status == null) {
            status = ReviewStatus.POSTED;
        }
        return Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(rating)
            .status(status)
            .build();
    }

}
