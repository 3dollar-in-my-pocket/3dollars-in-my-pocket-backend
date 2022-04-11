package com.depromeet.threedollar.api.user.service.review.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.user.collection.store.StoreDictionary;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewsCursorResponse {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailResponse> contents = new ArrayList<>();
    private long nextCursor;
    private boolean hasNext;

    private ReviewsCursorResponse(List<ReviewDetailResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
        this.hasNext = LAST_CURSOR != nextCursor;
    }

    public static ReviewsCursorResponse of(@NotNull CursorPagingSupporter<Review> reviewsCursor, @NotNull StoreDictionary storeDictionary, @NotNull User user) {
        List<ReviewDetailResponse> reviews = combineReviewDetailResponse(reviewsCursor.getItemsInCurrentCursor(), storeDictionary, user);
        if (reviewsCursor.hasNext()) {
            return new ReviewsCursorResponse(reviews, reviewsCursor.getNextCursor().getId());
        }
        return new ReviewsCursorResponse(reviews, LAST_CURSOR);
    }

    private static List<ReviewDetailResponse> combineReviewDetailResponse(List<Review> reviews, StoreDictionary storeDictionary, User user) {
        return reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, storeDictionary.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
    }

}
