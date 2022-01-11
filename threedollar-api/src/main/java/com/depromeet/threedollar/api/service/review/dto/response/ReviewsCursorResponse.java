package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.domain.common.collection.CursorSupporter;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.collection.store.StoreDictionary;
import com.depromeet.threedollar.domain.user.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewsCursorResponse {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailResponse> contents = new ArrayList<>();
    private long nextCursor;

    private ReviewsCursorResponse(List<ReviewDetailResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
    }

    public static ReviewsCursorResponse of(@NotNull CursorSupporter<Review> reviewsCursor, @NotNull StoreDictionary storeDictionary, @NotNull User user) {
        if (reviewsCursor.isLastCursor()) {
            return newLastCursor(reviewsCursor.getItemsInCurrentCursor(), storeDictionary, user);
        }
        return newCursorHasNext(reviewsCursor.getItemsInCurrentCursor(), storeDictionary, user, reviewsCursor.getNextCursor().getId());
    }

    private static ReviewsCursorResponse newLastCursor(List<Review> reviews, @NotNull StoreDictionary storeDictionary, @NotNull User user) {
        return newCursorHasNext(reviews, storeDictionary, user, LAST_CURSOR);
    }

    private static ReviewsCursorResponse newCursorHasNext(List<Review> reviews, @NotNull StoreDictionary storeDictionary, @NotNull User user, long nextCursor) {
        List<ReviewDetailResponse> contents = reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, storeDictionary.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
        return new ReviewsCursorResponse(contents, nextCursor);
    }

}
