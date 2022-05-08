package com.depromeet.threedollar.domain.rds.user.domain.review;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        @Nullable Integer rating,
        @Nullable ReviewStatus status
    ) {
        return Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(Optional.ofNullable(rating).orElse(3))
            .status(Optional.ofNullable(status).orElse(ReviewStatus.POSTED))
            .build();
    }

}
